#!/usr/bin/env bash
#
# Runs the instrumented suites against the Mock BFF on the alfie-bff-tests AVD, from nothing to a
# verified run:
#   1. refuses to start if anything already listens on the BFF port;
#   2. finds the alfie-bff-tests AVD, or installs, creates and boots it (headless, no snapshots);
#   3. builds the BFF (yarn) from ${ALFIE_BFF_PATH:-../Alfie-BFF} and boots the Mock BFF;
#   4. runs the suites in one Gradle call, then fails any suite that executed 0 tests;
#   5. on exit, Ctrl-C included, kills the BFF and shuts the AVD down if this script booted it.
#
# Every other emulator and device is ignored. The BFF checkout's node_modules and dist are written;
# its git state never is. The BFF log is left at build/bff-tests/bff.log.
#
# ALFIE_BFF_START_CMD overrides the BFF start command (run in the BFF checkout), e.g. to reach a
# Live BFF; the Mock BFF is the default and the only backend the suites are written for.

set -o pipefail

usage() {
    cat <<EOF
Usage: scripts/run-bff-tests.sh [--only <suite>]

Runs the instrumented suites against the Mock BFF on the alfie-bff-tests AVD.

Options:
  --only <suite>  Run one suite: $ALL_SUITES
  -h, --help      Show this help.

Environment:
  ALFIE_BFF_PATH       Alfie-BFF checkout (default: ../Alfie-BFF next to this repo).
  ALFIE_BFF_START_CMD  Override the BFF start command (default: the Mock BFF, node dist/main).
  ANDROID_HOME         Android SDK (default: ANDROID_SDK_ROOT, then sdk.dir in local.properties).
EOF
}

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUT_DIR="$PROJECT_DIR/build/bff-tests"
BFF_LOG="$OUT_DIR/bff.log"
BFF_PATH="${ALFIE_BFF_PATH:-$PROJECT_DIR/../Alfie-BFF}"
BFF_PORT=4000  # the app's DEV environment and BffIntegrationTest.BFF_URL both hard-code it

# --- Suites ----------------------------------------------------------------------------------------
# A suite is a name, the Gradle task that runs it, and the folder its result XMLs land in.

ALL_SUITES="integration"

suite_task() {
    case "$1" in
        integration) echo ":integration-test:connectedDebugAndroidTest" ;;
    esac
}

suite_results_dir() {
    case "$1" in
        integration) echo "$PROJECT_DIR/integration-test/build/outputs/androidTest-results/connected/debug" ;;
    esac
}

# --- Helpers ---------------------------------------------------------------------------------------

fail() { echo "❌ $*" >&2; exit 1; }
info() { echo "$*"; }
warn() { echo "⚠️  $*"; }

is_known_suite() {
    local suite
    for suite in $ALL_SUITES; do [ "$suite" = "$1" ] && return 0; done
    return 1
}

# --- Arguments -------------------------------------------------------------------------------------

SUITES="$ALL_SUITES"
while [ $# -gt 0 ]; do
    case "$1" in
        --only)
            [ -n "${2:-}" ] || { usage >&2; fail "--only needs a suite: $ALL_SUITES"; }
            is_known_suite "$2" || fail "Unknown suite '$2'. Suites: $ALL_SUITES"
            SUITES="$2"
            shift 2 ;;
        -h|--help) usage; exit 0 ;;
        *) usage >&2; fail "Unknown option: $1" ;;
    esac
done

# --- Port ------------------------------------------------------------------------------------------

fail_if_port_taken() {
    command -v lsof >/dev/null 2>&1 || fail "lsof not found; it's needed to check port $BFF_PORT is free."
    local pids pid holders=""
    pids="$(lsof -nP -iTCP:"$BFF_PORT" -sTCP:LISTEN -t 2>/dev/null | sort -u)"
    [ -n "$pids" ] || return 0
    for pid in $pids; do
        holders="$holders
   PID $pid: $(ps -o command= -p "$pid" 2>/dev/null || echo '(exited)')"
    done
    fail "Port $BFF_PORT is already in use, so the Mock BFF can't start there:$holders
   Stop it and run again. The suites only run against a BFF this script booted."
}

# --- Android SDK -----------------------------------------------------------------------------------

AVD_NAME="alfie-bff-tests"
AVD_API=34
AVD_TAG="aosp_atd"
case "$(uname -m)" in
    arm64|aarch64) AVD_ABI="arm64-v8a" ;;  # only arm64 images are accelerated on Apple Silicon
    *) AVD_ABI="x86_64" ;;
esac
AVD_IMAGE="system-images;android-$AVD_API;$AVD_TAG;$AVD_ABI"
AVD_BOOT_TIMEOUT=300

find_android_sdk() {
    local sdk="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
    if [ -z "$sdk" ] && [ -f "$PROJECT_DIR/local.properties" ]; then
        sdk="$(sed -n 's/^sdk\.dir=//p' "$PROJECT_DIR/local.properties")"
    fi
    [ -n "$sdk" ] && [ -d "$sdk" ] || fail "Android SDK not found. Set ANDROID_HOME."
    echo "$sdk"
}

# sdkmanager and avdmanager are only needed to install the image or create the AVD.
sdk_tool() {
    local tool="$1" path="$SDK/cmdline-tools/latest/bin/$1"
    [ -x "$path" ] || path="$(command -v "$tool" 2>/dev/null)"
    [ -n "$path" ] || fail "$tool not found. Install the Android SDK Command-line Tools (Android Studio > SDK Manager > SDK Tools)."
    echo "$path"
}

# Serials of every running emulator whose AVD is alfie-bff-tests.
serials_of_avd() {
    local serial name
    for serial in $("$ADB" devices | awk 'NR > 1 && $1 ~ /^emulator-/ {print $1}'); do
        name="$("$ADB" -s "$serial" emu avd name 2>/dev/null | head -1 | tr -d '\r')"
        [ "$name" = "$AVD_NAME" ] && echo "$serial"
    done
}

ensure_avd_exists() {
    local tool
    if [ ! -d "$SDK/system-images/android-$AVD_API/$AVD_TAG/$AVD_ABI" ]; then
        tool="$(sdk_tool sdkmanager)" || exit 1
        info "📥 Installing $AVD_IMAGE (about 590 MB download)..."
        "$tool" "$AVD_IMAGE" || fail "sdkmanager couldn't install $AVD_IMAGE."
    fi
    if ! "$EMULATOR" -list-avds 2>/dev/null | grep -qx "$AVD_NAME"; then
        tool="$(sdk_tool avdmanager)" || exit 1
        info "🆕 Creating the $AVD_NAME AVD ($AVD_IMAGE)..."
        echo no | "$tool" create avd -n "$AVD_NAME" -k "$AVD_IMAGE" \
            || fail "avdmanager couldn't create the $AVD_NAME AVD."
        echo  # avdmanager's hardware-profile prompt ends without a newline
    fi
}

# Boots the AVD in its own process group, so Ctrl-C reaches it only through clean-up.
start_avd() {
    info "🚀 Booting $AVD_NAME headless (log: $OUT_DIR/emulator.log)..."
    set -m
    "$EMULATOR" -avd "$AVD_NAME" -no-window -no-snapshot -no-audio -no-boot-anim -gpu swiftshader_indirect \
        > "$OUT_DIR/emulator.log" 2>&1 &
    EMULATOR_PID=$!
    set +m
}

wait_for_started_avd() {
    local waited=0
    info "⏳ Waiting for $AVD_NAME to boot (timeout ${AVD_BOOT_TIMEOUT}s)..."
    while [ "$waited" -lt "$AVD_BOOT_TIMEOUT" ]; do
        kill -0 "$EMULATOR_PID" 2>/dev/null || fail "The emulator exited while booting. See $OUT_DIR/emulator.log."
        [ -n "$SERIAL" ] || SERIAL="$(serials_of_avd | head -1)"
        if [ -n "$SERIAL" ] && [ "$("$ADB" -s "$SERIAL" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ]; then
            return 0
        fi
        sleep 1
        waited=$((waited + 1))
    done
    fail "$AVD_NAME didn't boot within ${AVD_BOOT_TIMEOUT}s. See $OUT_DIR/emulator.log."
}

disable_animations() {
    local scale
    for scale in window_animation_scale transition_animation_scale animator_duration_scale; do
        "$ADB" -s "$SERIAL" shell settings put global "$scale" 0 || fail "Couldn't turn off $scale on $SERIAL."
    done
}

# --- BFF -------------------------------------------------------------------------------------------

BFF_READY_TIMEOUT=60
GRAPHQL_URL="http://localhost:$BFF_PORT/graphql"

warn_if_bff_checkout_differs_from_main() {
    git -C "$BFF_PATH" rev-parse --git-dir >/dev/null 2>&1 || return 0
    local branch head remote_main
    head="$(git -C "$BFF_PATH" rev-parse HEAD 2>/dev/null)"
    branch="$(git -C "$BFF_PATH" symbolic-ref --short -q HEAD || echo "a detached HEAD")"
    remote_main="$(GIT_TERMINAL_PROMPT=0 GIT_SSH_COMMAND="ssh -o BatchMode=yes -o ConnectTimeout=5" \
        git -C "$BFF_PATH" ls-remote origin refs/heads/main 2>/dev/null | cut -f1)"
    # CI checks out main's commit as a detached HEAD; that is main.
    if [ "$branch" != "main" ] && [ "$head" != "$remote_main" ]; then
        warn "The BFF checkout at $BFF_PATH is on $branch, not main. CI tests against main."
    fi
    if [ -n "$remote_main" ] && ! git -C "$BFF_PATH" merge-base --is-ancestor "$remote_main" HEAD 2>/dev/null; then
        warn "The BFF checkout at $BFF_PATH is behind origin/main ($remote_main). CI tests against main."
    fi
}

# Advisory checks: each prints a warning and never fails the run.
pre_run_warnings() {
    warn_if_bff_checkout_differs_from_main
}

build_bff() {
    info "📦 Building the BFF (yarn install --frozen-lockfile, yarn build)..."
    ( cd "$BFF_PATH" && yarn install --frozen-lockfile && yarn build ) || fail "The BFF build failed in $BFF_PATH."
}

# Starts the BFF in its own process group, so clean-up can kill it and every child it spawns.
start_bff() {
    local start_cmd
    if [ -n "${ALFIE_BFF_START_CMD:-}" ]; then
        start_cmd="$ALFIE_BFF_START_CMD"
        warn "Starting the BFF with ALFIE_BFF_START_CMD; the suites are written for the Mock BFF."
    else
        # Auth is off and the JWT secret is thrown away with the run. Explicit env beats the BFF's .env.
        BFF_JWT_SECRET="$(openssl rand -hex 32)" || fail "openssl couldn't generate a JWT secret."
        export BFF_JWT_SECRET
        start_cmd="PLATFORM=mock AUTH_DISABLED=true PORT=$BFF_PORT MOCK_ASSETS_BASE_URL=http://10.0.2.2:$BFF_PORT node dist/main"
    fi
    info "🚀 Starting the BFF: $start_cmd (log: $BFF_LOG)"
    set -m
    ( cd "$BFF_PATH" && exec bash -c "$start_cmd" ) > "$BFF_LOG" 2>&1 &
    BFF_PGID=$!
    set +m
}

wait_for_bff() {
    local waited=0 body
    info "⏳ Waiting for GraphQL at $GRAPHQL_URL (timeout ${BFF_READY_TIMEOUT}s)..."
    while [ "$waited" -lt "$BFF_READY_TIMEOUT" ]; do
        kill -0 "$BFF_PGID" 2>/dev/null || fail_with_bff_log "The BFF exited before it was ready."
        body="$(curl -s -m 2 -X POST "$GRAPHQL_URL" -H 'Content-Type: application/json' \
            --data '{"query":"{__typename}"}' 2>/dev/null)"
        if printf '%s' "$body" | grep -q '"data"'; then
            info "✅ The BFF is ready."
            return 0
        fi
        sleep 1
        waited=$((waited + 1))
    done
    fail_with_bff_log "The BFF didn't answer GraphQL at $GRAPHQL_URL within ${BFF_READY_TIMEOUT}s."
}

fail_with_bff_log() {
    echo "--- last lines of $BFF_LOG ---" >&2
    tail -20 "$BFF_LOG" >&2
    fail "$1"
}

# --- Tests -----------------------------------------------------------------------------------------

# Prints "<executed> <skipped>" for the test cases in a suite's result XMLs. A case is skipped when it
# has <skipped/> or fails on an assumption: on a device, AGP writes a JUnit 5 TestAbortedException
# as a <failure> and still passes the task, so a suite whose BFF died looks green.
count_tests() {
    cat "$1"/*.xml 2>/dev/null | awk '
        /<testcase[ >]/ { total++; in_case = 1; marked = 0 }
        in_case && !marked && (/<skipped/ || /<failure[^>]*>(org\.opentest4j\.TestAbortedException|org\.junit\.(internal\.)?AssumptionViolatedException)/) {
            skipped++; marked = 1
        }
        in_case && (/<\/testcase>/ || /<testcase[^>]*\/>/) { in_case = 0 }
        END { print total - skipped, skipped + 0 }'
}

run_suites() {
    local suite tasks="" gradle_rc guard_rc=0 executed skipped
    for suite in $SUITES; do
        rm -rf "$(suite_results_dir "$suite")"  # stale results must never count
        tasks="$tasks $(suite_task "$suite")"
    done

    info "🧪 Running:$tasks on $SERIAL"
    # shellcheck disable=SC2086 # one word per task
    ( cd "$PROJECT_DIR" && ANDROID_SERIAL="$SERIAL" ./gradlew $tasks --continue )
    gradle_rc=$?

    # Gradle reports a run of 0 tests as green, so every suite must show executed tests.
    for suite in $SUITES; do
        read -r executed skipped <<EOF
$(count_tests "$(suite_results_dir "$suite")")
EOF
        if [ "$executed" -gt 0 ]; then
            info "📊 $suite: $executed executed, $skipped skipped"
        else
            echo "❌ $suite: 0 tests executed ($skipped skipped). Every test was skipped or none ran; see $(suite_results_dir "$suite")." >&2
            guard_rc=1
        fi
    done

    if [ "$gradle_rc" -ne 0 ]; then
        echo "❌ Gradle failed (exit $gradle_rc). Reports: */build/reports/androidTests/connected/" >&2
        return 1
    fi
    return "$guard_rc"
}

# --- Clean-up --------------------------------------------------------------------------------------

BFF_PGID=""
EMULATOR_PID=""
SERIAL=""

cleanup() {
    if [ -n "$BFF_PGID" ]; then
        info "🧹 Stopping the BFF (log: $BFF_LOG)"
        kill -TERM -- -"$BFF_PGID" 2>/dev/null
        local i
        for i in 1 2 3 4 5; do kill -0 -- -"$BFF_PGID" 2>/dev/null || break; sleep 1; done
        kill -KILL -- -"$BFF_PGID" 2>/dev/null
    fi
    if [ -n "$EMULATOR_PID" ]; then
        info "🧹 Shutting down $AVD_NAME (booted by this run)"
        # No other alfie-bff-tests was running when this run booted one, so any serial found is ours.
        [ -n "$SERIAL" ] || SERIAL="$(serials_of_avd | head -1)"
        [ -n "$SERIAL" ] && "$ADB" -s "$SERIAL" emu kill >/dev/null 2>&1
        local j
        for j in $(seq 1 30); do kill -0 "$EMULATOR_PID" 2>/dev/null || break; sleep 1; done
        kill -TERM -- -"$EMULATOR_PID" 2>/dev/null
    fi
}

# --- Main ------------------------------------------------------------------------------------------

fail_if_port_taken

for tool in node yarn curl openssl git; do
    command -v "$tool" >/dev/null 2>&1 || fail "$tool not found; it's needed to build and run the BFF."
done
[ -d "$BFF_PATH" ] || fail "No BFF checkout at $BFF_PATH. Clone Alfie-BFF next to this repo or set ALFIE_BFF_PATH."
BFF_PATH="$(cd "$BFF_PATH" && pwd)"
SDK="$(find_android_sdk)" || exit 1
ADB="$SDK/platform-tools/adb"
EMULATOR="$SDK/emulator/emulator"
[ -x "$ADB" ] || fail "adb not found at $ADB."
[ -x "$EMULATOR" ] || fail "The emulator isn't installed at $EMULATOR."

mkdir -p "$OUT_DIR"
trap cleanup EXIT
trap 'exit 130' INT
trap 'exit 143' TERM

info "🔖 BFF: $(git -C "$BFF_PATH" rev-parse HEAD 2>/dev/null || echo unknown) ($BFF_PATH)"
pre_run_warnings

SERIAL="$(serials_of_avd | head -1)"
if [ -n "$SERIAL" ]; then
    info "📱 Reusing the running $AVD_NAME ($SERIAL); it stays running."
else
    ensure_avd_exists
    start_avd
fi

build_bff
start_bff
wait_for_bff

[ -n "$EMULATOR_PID" ] && wait_for_started_avd
info "📱 Device: $SERIAL ($AVD_NAME)"
disable_animations

run_suites
