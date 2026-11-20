#!/usr/bin/env bash
#
# Scenario tests for scripts/run-bff-tests.sh. Each scenario copies the script into a throwaway
# sandbox whose Android SDK (adb, emulator, sdkmanager, avdmanager), Gradle wrapper, yarn, node,
# curl and lsof are stubs that record their calls. git, ps, openssl and process groups are real.
# Nothing touches a real emulator, device, BFF or port.
#
# Usage: scripts/tests/run-bff-tests-test.sh [scenario-name ...]

set -u

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SCRIPT_UNDER_TEST="$HERE/../run-bff-tests.sh"
ROOT_TMP="$(cd "$(mktemp -d "${TMPDIR:-/tmp}/run-bff-tests-test.XXXXXX")" && pwd -P)"
trap 'rm -rf "$ROOT_TMP"' EXIT

PASSED=0
FAILED=0
FAILURES=""

# --- Sandbox -------------------------------------------------------------------------------------

write_stub() { # path, body
    mkdir -p "$(dirname "$1")"
    printf '#!/usr/bin/env bash\n%s\n' "$2" > "$1"
    chmod +x "$1"
}

setup_sandbox() {
    SANDBOX="$ROOT_TMP/$1"
    PROJ="$SANDBOX/project"
    SDK="$SANDBOX/sdk"
    BFF="$SANDBOX/Alfie-BFF"
    STATE="$SANDBOX/state"
    mkdir -p "$PROJ/scripts" "$SDK" "$BFF" "$STATE" "$SANDBOX/bin"
    cp "$SCRIPT_UNDER_TEST" "$PROJ/scripts/run-bff-tests.sh"
    : > "$STATE/calls.log"
    : > "$STATE/devices"   # "<serial> <avd name or ->", one per line
    : > "$STATE/avds"      # existing AVD names
    # The developer's own emulator and a USB phone are always attached; nothing may touch them.
    echo "emulator-5554 Pixel_9_Pro_36_Ggl_API" >> "$STATE/devices"
    echo "R58M12345 -" >> "$STATE/devices"
    echo "Pixel_9_Pro_36_Ggl_API" >> "$STATE/avds"

    # A BFF checkout on main, level with its origin.
    git init -q --bare "$SANDBOX/bff-origin.git"
    git -C "$BFF" init -q -b main
    git -C "$BFF" -c user.name=t -c user.email=t@t commit -q --allow-empty -m "bff initial"
    git -C "$BFF" remote add origin "$SANDBOX/bff-origin.git"
    git -C "$BFF" push -q origin main

    local s="STATE='$STATE'"

    write_stub "$SDK/platform-tools/adb" "$s"'
echo "adb $*" >> "$STATE/calls.log"
serial=""
if [ "$1" = "-s" ]; then serial="$2"; shift 2; fi
case "$1" in
    devices)
        echo "List of devices attached"
        while read -r s _; do [ -n "$s" ] && printf "%s\tdevice\n" "$s"; done < "$STATE/devices"
        echo ;;
    emu)
        case "$2" in
            avd) name="$(awk -v s="$serial" '"'"'$1==s {print $2}'"'"' "$STATE/devices")"
                 printf "%s\r\nOK\r\n" "$name" ;;
            kill) grep -v "^$serial " "$STATE/devices" > "$STATE/devices.tmp"; mv "$STATE/devices.tmp" "$STATE/devices"
                  [ -f "$STATE/emulator.pid" ] && kill "$(cat "$STATE/emulator.pid")" 2>/dev/null
                  echo OK ;;
        esac ;;
    wait-for-device) ;;
    shell)
        case "$*" in *"getprop sys.boot_completed"*) echo 1 ;; esac ;;
esac'

    write_stub "$SDK/emulator/emulator" "$s"'
echo "emulator $*" >> "$STATE/calls.log"
if [ "$1" = "-list-avds" ]; then cat "$STATE/avds"; exit 0; fi
name=""
while [ $# -gt 0 ]; do [ "$1" = "-avd" ] && name="$2"; shift; done
echo "emulator-5556 $name" >> "$STATE/devices"
echo $$ > "$STATE/emulator.pid"
exec sleep 300'

    write_stub "$SDK/cmdline-tools/latest/bin/sdkmanager" "$s"'
echo "sdkmanager $*" >> "$STATE/calls.log"
IFS=";" read -r _ api tag abi <<< "$1"
mkdir -p "'"$SDK"'/system-images/$api/$tag/$abi"'

    write_stub "$SDK/cmdline-tools/latest/bin/avdmanager" "$s"'
cat > /dev/null
echo "avdmanager $*" >> "$STATE/calls.log"
while [ $# -gt 0 ]; do [ "$1" = "-n" ] && echo "$2" >> "$STATE/avds"; shift; done'

    write_stub "$SANDBOX/bin/yarn" "$s"'
echo "yarn $* (in $PWD)" >> "$STATE/calls.log"'

    # The BFF: `node dist/main` with a child process, so only a process-group kill clears it.
    write_stub "$SANDBOX/bin/node" "$s"'
echo "node $* PLATFORM=$PLATFORM AUTH_DISABLED=$AUTH_DISABLED PORT=$PORT MOCK_ASSETS_BASE_URL=$MOCK_ASSETS_BASE_URL JWT_LEN=${#BFF_JWT_SECRET}" >> "$STATE/calls.log"
echo "fake BFF starting"
[ "$(cat "$STATE/bff_mode" 2>/dev/null)" = "die" ] && { echo "fake BFF crashed"; exit 1; }
sleep 300 &
echo "$! $$" >> "$STATE/bff_pids"
touch "$STATE/bff_up"
wait'

    write_stub "$SANDBOX/bin/curl" "$s"'
[ -f "$STATE/bff_up" ] || exit 7
echo "{\"data\":{\"__typename\":\"Query\"}}"'

    write_stub "$SANDBOX/bin/lsof" "$s"'
[ -f "$STATE/port_holder.pid" ] || exit 1
pid="$(cat "$STATE/port_holder.pid")"
case " $* " in *" -t "*) echo "$pid" ;; *) echo "COMMAND PID USER"; echo "fake $pid me TCP *:4000 (LISTEN)" ;; esac'

    write_stub "$PROJ/gradlew" "$s"'
echo "gradlew $* ANDROID_SERIAL=${ANDROID_SERIAL:-} JWT_SEEN=${BFF_JWT_SECRET:+yes}" >> "$STATE/calls.log"
mode="$(cat "$STATE/gradle_mode" 2>/dev/null || echo pass)"
# One <testcase> per outcome, written the way AGP writes on-device results: a JUnit 5 assumption
# failure is a <failure> holding a TestAbortedException, not a <skipped/>.
write_results() { # outcome... (pass | abort | skip | fail)
    local dir="$PWD/integration-test/build/outputs/androidTest-results/connected/debug" n=0 outcome
    mkdir -p "$dir"
    {
        echo "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
        echo "<testsuites tests=\"$#\" failures=\"0\" errors=\"0\" skipped=\"0\" time=\"0.000\">"
        echo "  <testsuite name=\"com.mindera.alfie.integrationtest.ProductListIntegrationTest\" tests=\"$#\" failures=\"0\" errors=\"0\" skipped=\"0\" time=\"1.0\">"
        for outcome in "$@"; do
            n=$((n + 1))
            case "$outcome" in
                pass) echo "    <testcase name=\"t$n\" classname=\"C\" time=\"0.5\" />" ;;
                abort) echo "    <testcase name=\"t$n\" classname=\"C\" time=\"0.0\">"
                       echo "      <failure>org.opentest4j.TestAbortedException: Assumption failed: BFF not reachable"
                       echo "	at com.mindera.alfie.integrationtest.BffIntegrationTest.assumeBffReachable(BffIntegrationTest.kt:33)"
                       echo "</failure>"
                       echo "    </testcase>" ;;
                skip) echo "    <testcase name=\"t$n\" classname=\"C\" time=\"0.0\">"
                      echo "      <skipped />"
                      echo "    </testcase>" ;;
                fail) echo "    <testcase name=\"t$n\" classname=\"C\" time=\"0.5\">"
                      echo "      <failure>java.lang.AssertionError: expected a next page"
                      echo "	at org.opentest4j.TestAbortedException.mentionedInATrace(Nowhere.kt:1)"
                      echo "</failure>"
                      echo "    </testcase>" ;;
            esac
        done
        echo "  </testsuite>"
        echo "</testsuites>"
    } > "$dir/TEST-alfie-bff-tests(AVD) - 14.xml"
}
case "$mode" in
    pass) write_results pass pass pass pass pass pass pass ;;
    aborted) write_results abort abort abort abort abort abort abort ;;
    skipped) write_results skip skip skip skip skip skip skip ;;
    partly) write_results pass pass pass abort abort skip skip ;;
    fail) write_results pass pass pass pass pass pass fail; exit 1 ;;
    noresults) ;;
    hang) touch "$STATE/gradle_started"; exec sleep 300 ;;
esac'
}

# Runs the sandboxed script with the stubs first on PATH.
sandboxed_script() {
    cd "$PROJ" && env -u ANDROID_SERIAL -u ANDROID_SDK_ROOT -u BFF_JWT_SECRET \
        PATH="$SANDBOX/bin:$PATH" ANDROID_HOME="$SDK" ALFIE_BFF_PATH="$BFF" \
        bash ${TRACE:+-x} scripts/run-bff-tests.sh "$@"
}

# Runs the sandboxed script; sets OUT and RC.
run_script() {
    OUT="$(sandboxed_script "$@" 2>&1)"
    RC=$?
}

called() { grep -q -- "$1" "$STATE/calls.log"; }

bff_processes_alive() {
    [ -f "$STATE/bff_pids" ] || return 1
    local pid
    for pid in $(cat "$STATE/bff_pids"); do kill -0 "$pid" 2>/dev/null && return 0; done
    return 1
}

# --- Assertions ----------------------------------------------------------------------------------

CURRENT=""
CURRENT_OK=true
flunk() {
    CURRENT_OK=false
    echo "    ✗ $1"
}
assert_rc() { [ "$RC" -eq "$1" ] || flunk "expected exit $1, got $RC"; }
assert_rc_nonzero() { [ "$RC" -ne 0 ] || flunk "expected a non-zero exit, got 0"; }
assert_out() { printf '%s' "$OUT" | grep -q -- "$1" || flunk "output lacks: $1"; }
assert_called() { called "$1" || flunk "never called: $1"; }
assert_not_called() { ! called "$1" || flunk "unexpectedly called: $1 ($(grep -- "$1" "$STATE/calls.log" | head -1))"; }
# Other devices may only be asked which AVD they are.
assert_other_devices_untouched() {
    local touched
    touched="$(grep -e "adb -s emulator-5554" -e "adb -s R58M12345" "$STATE/calls.log" | grep -v " emu avd name$")"
    [ -z "$touched" ] || flunk "touched another device: $touched"
    assert_not_called "adb -s R58M12345"
    assert_not_called "adb emu"
    assert_not_called "adb shell"
}

dump() {
    echo "    --- output ---"; printf '%s\n' "$OUT" | sed 's/^/    | /'
    echo "    --- calls ---"; sed 's/^/    | /' "$STATE/calls.log"
}

run_scenario() {
    CURRENT="$1"
    CURRENT_OK=true
    echo "• $1"
    setup_sandbox "$1"
    OUT=""; RC=0
    "$1"
    [ -n "${TRACE:-}" ] && dump
    if [ "$CURRENT_OK" = true ]; then
        PASSED=$((PASSED + 1))
    else
        dump
        FAILED=$((FAILED + 1))
        FAILURES="$FAILURES $1"
    fi
}

# --- Scenarios -----------------------------------------------------------------------------------

port_clash_fails_before_booting_and_names_the_holder() {
    bash -c 'exec -a fake-dev-bff sleep 300' &
    local holder=$!
    echo "$holder" > "$STATE/port_holder.pid"

    run_script

    { kill "$holder"; wait "$holder"; } 2>/dev/null
    assert_rc_nonzero
    assert_out "4000"
    assert_out "$holder"
    assert_out "fake-dev-bff"
    assert_not_called "emulator -avd"
    assert_not_called "yarn"
    assert_not_called "node"
    assert_not_called "gradlew"
}

cold_run_installs_creates_boots_runs_and_shuts_down_its_own_avd() {
    run_script

    assert_rc 0
    # The image, the AVD and a headless boot without snapshot state, for the host's ABI.
    assert_called "sdkmanager system-images;android-34;aosp_atd;$EXPECTED_ABI"
    assert_out "590 MB"
    assert_called "avdmanager create avd -n alfie-bff-tests -k system-images;android-34;aosp_atd;$EXPECTED_ABI"
    assert_called "emulator -avd alfie-bff-tests .*-no-window"
    assert_called "emulator -avd alfie-bff-tests .*-no-snapshot"
    assert_called "adb -s emulator-5556 shell settings put global window_animation_scale 0"
    assert_called "adb -s emulator-5556 shell settings put global transition_animation_scale 0"
    assert_called "adb -s emulator-5556 shell settings put global animator_duration_scale 0"
    # The BFF: built, booted as the Mock BFF with a fresh secret, and its SHA printed.
    assert_called "yarn install --frozen-lockfile (in $BFF)"
    assert_called "yarn build (in $BFF)"
    assert_called "node dist/main PLATFORM=mock AUTH_DISABLED=true PORT=4000 MOCK_ASSETS_BASE_URL=http://10.0.2.2:4000 JWT_LEN=64"
    assert_out "$(git -C "$BFF" rev-parse HEAD)"
    # One Gradle call, pinned to the AVD.
    assert_called "gradlew :integration-test:connectedDebugAndroidTest --continue ANDROID_SERIAL=emulator-5556"
    assert_out "integration: 7 executed, 0 skipped"
    # Clean-up: the AVD it booted goes down, the BFF is gone, the log stays.
    assert_called "adb -s emulator-5556 emu kill"
    bff_processes_alive && flunk "BFF processes are still running"
    [ -s "$PROJ/build/bff-tests/bff.log" ] || flunk "no BFF log at build/bff-tests/bff.log"
    grep -q "fake BFF starting" "$PROJ/build/bff-tests/bff.log" || flunk "BFF log lacks the BFF's output"
    assert_other_devices_untouched
}

reuses_a_running_avd_and_leaves_it_running() {
    echo "alfie-bff-tests" >> "$STATE/avds"
    echo "emulator-5558 alfie-bff-tests" >> "$STATE/devices"

    run_script

    assert_rc 0
    assert_out "Reusing"
    assert_not_called "emulator -avd"
    assert_not_called "sdkmanager"
    assert_not_called "avdmanager"
    assert_called "gradlew .* ANDROID_SERIAL=emulator-5558"
    assert_not_called "emu kill"
    assert_other_devices_untouched
}

# On a device, AGP records JUnit 5 assumption failures as <failure>s and still exits 0.
aborted_tests_do_not_count_as_executed() {
    echo aborted > "$STATE/gradle_mode"

    run_script

    assert_rc_nonzero
    assert_out "integration: 0 tests executed"
}

skipped_tests_do_not_count_as_executed() {
    echo skipped > "$STATE/gradle_mode"

    run_script

    assert_rc_nonzero
    assert_out "integration: 0 tests executed"
}

a_partly_skipped_suite_passes_and_reports_its_counts() {
    echo partly > "$STATE/gradle_mode"

    run_script

    assert_rc 0
    assert_out "integration: 3 executed, 4 skipped"
}

results_from_an_earlier_run_never_count() {
    echo pass > "$STATE/gradle_mode"
    run_script
    assert_rc 0
    echo noresults > "$STATE/gradle_mode"

    run_script

    assert_rc_nonzero
    assert_out "integration: 0 tests executed"
}

a_gradle_failure_fails_the_run() {
    echo fail > "$STATE/gradle_mode"

    run_script

    assert_rc_nonzero
    assert_out "Gradle failed"
    bff_processes_alive && flunk "BFF processes are still running"
}

ctrl_c_mid_run_leaves_no_bff_behind() {
    echo hang > "$STATE/gradle_mode"
    local log="$SANDBOX/ctrl-c.out" pid waited=0
    # Job control gives the script its own process group, as a terminal would; Ctrl-C signals it all.
    set -m
    sandboxed_script > "$log" 2>&1 &
    pid=$!
    set +m
    while [ ! -f "$STATE/gradle_started" ] && [ "$waited" -lt 30 ]; do sleep 1; waited=$((waited + 1)); done
    bff_processes_alive || flunk "the BFF wasn't running mid-run"

    kill -INT -- -"$pid"
    wait "$pid"
    RC=$?
    OUT="$(cat "$log")"

    assert_rc_nonzero
    bff_processes_alive && flunk "BFF processes survived Ctrl-C"
    assert_called "adb -s emulator-5556 emu kill"
    [ -s "$PROJ/build/bff-tests/bff.log" ] || flunk "no BFF log after Ctrl-C"
}

a_bff_that_dies_before_ready_fails_with_its_log() {
    echo die > "$STATE/bff_mode"

    run_script

    assert_rc_nonzero
    assert_out "exited before it was ready"
    assert_out "fake BFF crashed"
    assert_not_called "gradlew"
    assert_called "adb -s emulator-5556 emu kill"
}

warns_about_a_bff_checkout_off_main_or_behind_without_touching_it() {
    git -C "$BFF" checkout -q -b feature
    # origin/main moves ahead of the checkout.
    local other="$SANDBOX/other-clone"
    git clone -q "$SANDBOX/bff-origin.git" "$other"
    git -C "$other" -c user.name=t -c user.email=t@t commit -q --allow-empty -m "newer bff"
    git -C "$other" push -q origin main
    local head_before
    head_before="$(git -C "$BFF" rev-parse HEAD)"

    run_script

    assert_rc 0
    assert_out "on feature, not main"
    assert_out "behind origin/main"
    [ "$(git -C "$BFF" rev-parse HEAD)" = "$head_before" ] || flunk "the BFF checkout's HEAD moved"
    [ "$(git -C "$BFF" symbolic-ref --short HEAD)" = "feature" ] || flunk "the BFF checkout's branch changed"
}

no_warnings_for_a_bff_checkout_level_with_main() {
    run_script

    assert_rc 0
    printf '%s' "$OUT" | grep -q "⚠️" && flunk "unexpected warning: $(printf '%s' "$OUT" | grep "⚠️")"
}

only_integration_runs_the_integration_suite() {
    run_script --only integration

    assert_rc 0
    assert_called "gradlew :integration-test:connectedDebugAndroidTest --continue"
}

an_unknown_suite_is_refused_before_anything_starts() {
    run_script --only nope

    assert_rc_nonzero
    assert_out "Unknown suite 'nope'"
    assert_not_called "emulator -avd"
    assert_not_called "node"
}

the_jwt_secret_reaches_only_the_bff() {
    run_script

    assert_rc 0
    assert_called "JWT_LEN=64"
    assert_not_called "JWT_SEEN=yes"
    printf '%s' "$OUT" | grep -q "BFF_JWT_SECRET=" && flunk "the secret is printed"
}

an_unreachable_origin_on_main_s_commit_raises_no_branch_warning() {
    git -C "$BFF" checkout -q --detach  # how CI checks out main
    git -C "$BFF" remote set-url origin "$SANDBOX/no-such-origin.git"

    run_script

    assert_rc 0
    printf '%s' "$OUT" | grep -q "not main" && flunk "warned about the branch: $(printf '%s' "$OUT" | grep "not main")"
    assert_out "Couldn't read origin/main"
}

# --- Main ----------------------------------------------------------------------------------------

case "$(uname -m)" in arm64|aarch64) EXPECTED_ABI=arm64-v8a ;; *) EXPECTED_ABI=x86_64 ;; esac

ALL_SCENARIOS="
port_clash_fails_before_booting_and_names_the_holder
cold_run_installs_creates_boots_runs_and_shuts_down_its_own_avd
reuses_a_running_avd_and_leaves_it_running
aborted_tests_do_not_count_as_executed
skipped_tests_do_not_count_as_executed
a_partly_skipped_suite_passes_and_reports_its_counts
results_from_an_earlier_run_never_count
a_gradle_failure_fails_the_run
ctrl_c_mid_run_leaves_no_bff_behind
a_bff_that_dies_before_ready_fails_with_its_log
warns_about_a_bff_checkout_off_main_or_behind_without_touching_it
no_warnings_for_a_bff_checkout_level_with_main
only_integration_runs_the_integration_suite
an_unknown_suite_is_refused_before_anything_starts
the_jwt_secret_reaches_only_the_bff
an_unreachable_origin_on_main_s_commit_raises_no_branch_warning
"

for scenario in ${*:-$ALL_SCENARIOS}; do
    run_scenario "$scenario"
done

echo ""
echo "$PASSED passed, $FAILED failed${FAILURES:+:$FAILURES}"
[ "$FAILED" -eq 0 ]
