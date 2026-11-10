# CI emulator: cost and boot reliability on GitHub-hosted runners

Research for [#84](https://github.com/Mindera/Alfie-Android/issues/84) (map [#80](https://github.com/Mindera/Alfie-Android/issues/80), unblocks [#87](https://github.com/Mindera/Alfie-Android/issues/87)). Gathered 2026-11-10.

Question: what does the CI emulator cost, and how reliably does it boot, on GitHub-hosted Ubuntu runners, for an API 34 x86_64 AVD driven by `reactivecircus/android-emulator-runner`?

Each claim is tagged as **confirmed** (a primary source says so, or a run log shows it) or **estimate** (my inference from the sources).

## TL;DR

| # | Answer |
| --- | --- |
| 1 | `aosp_atd`, `google_atd` and `google_apis` all exist for API 34 x86_64. The action's own CI runs `34 / aosp_atd / x86_64` on `ubuntu-24.04` and passes. ATD downloads are less than half the size (586 MB vs 1,491 MB). Boot times are similar: about 25 s cold for ATD vs 33–37 s for the other images. ATD drops Chrome, Browser2, the Play Store and SystemUI. Google's removal list doesn't name WebView. None of what's dropped is needed by the in-scope tests, because UI runs skip Firebase and web views are out of scope. **Use `aosp_atd`**, and switch to `google_atd` only if Play Services turns out to be needed. |
| 2 | KVM works on `ubuntu-latest` once the documented udev rule is added (**confirmed**). Use the README's two-step AVD snapshot cache. A snapshot restore boots in 4–10 s. **The catch:** both the AVD cache and the Gradle cache only help PRs and the merge queue when a **push to `main`** writes them. `pull_request` and `merge_group` runs can't share what they write, and entries expire after 7 days unused. |
| 3 | Total for the `bff-tests` job: roughly **16–20 min cold** and **8–12 min warm** (**estimate**). That leaves at least 2× headroom under a 45-min `timeout-minutes` and the merge queue's 60-min check timeout. Gradle is the largest component. |
| 4 | **$0.** Standard GitHub-hosted runners are "free and unlimited" for public repos, and Alfie-Android is public (**confirmed**). Only larger runners are billed. |

## 1. ATD vs `google_apis` for API 34

### Availability: confirmed

Google's SDK repository manifests (`https://dl.google.com/android/repository/sys-img/<tag>/sys-img2-4.xml`, fetched 2026-11-10) list:

| Package | Rev | Download |
| --- | --- | --- |
| `system-images;android-34;aosp_atd;x86_64` | 2 | 586 MB |
| `system-images;android-34;google_atd;x86_64` | 1 | 667 MB |
| `system-images;android-34;google_apis;x86_64` | 14 | 1,491 MB |

ATD x86_64 images exist for API 30–36. arm64-v8a variants of all three exist for API 34.

The [Gradle managed devices page](https://developer.android.com/studio/test/gradle-managed-devices#use-atds) still has a code comment that says "ATDs currently support only API level 30". The manifest above contradicts it, so the comment is stale.

android-emulator-runner lists `aosp_atd` and `google_atd` as valid `target` values ([README, Configurations](https://github.com/ReactiveCircus/android-emulator-runner#configurations)). Its own CI matrix includes `ubuntu-24.04 / api-level 34 / aosp_atd / x86_64` ([main.yml](https://github.com/ReactiveCircus/android-emulator-runner/blob/main/.github/workflows/main.yml)). That's the strongest evidence that this exact combination is maintained.

### Boot time: confirmed from run logs (small sample)

| Image | Runner | SDK install (download) | Cold boot | Snapshot boot | Source |
| --- | --- | --- | --- | --- | --- |
| API 34 `aosp_atd` x86_64 | ubuntu-24.04 | ~55 s | 25 s | 4 s | [action CI job 117329234032](https://github.com/ReactiveCircus/android-emulator-runner/actions/runs/38912362990/job/117329234032) |
| API 34 `aosp_atd` x86_64 | ubuntu-24.04 | n/a | 27 s | 10 s | [action CI job 118362027182](https://github.com/ReactiveCircus/android-emulator-runner/actions/runs/39340338930/job/118362027182) |
| API 35 `google_apis` x86_64 | ubuntu-latest | ~29 s | 37 s | 4 s | [action CI job 117329233777](https://github.com/ReactiveCircus/android-emulator-runner/actions/runs/38912362990/job/117329233777) |
| API 34 `default` x86_64, no snapshot | ubuntu-24.04 | 16 s | 33 s | n/a | [nowinandroid job 100362153166](https://github.com/android/nowinandroid/actions/runs/33664309376/job/100362153166) |

How these were measured:
- **Cold boot** runs from the "Starting emulator." line to the "Emulator booted." line. The action prints "Emulator booted." once `getprop sys.boot_completed` returns 1 ([emulator-manager.ts `waitForDevice`](https://github.com/ReactiveCircus/android-emulator-runner/blob/main/src/emulator-manager.ts)).
- **Snapshot boot** is the second emulator step in the same job, which restores the `default_boot` snapshot. The log reports "Successfully loaded snapshot 'default_boot' using 648 ms".

I found no public `google_apis` API 34 log that's still retained, so the google_apis row is API 35. **Estimate:** `google_apis` 34 boots in about 30–40 s cold. Its main extra cost is the download, which is 2.5× the size of ATD.

The default `emulator-boot-timeout` is 600 s ([README](https://github.com/ReactiveCircus/android-emulator-runner#configurations)), about 15× the observed cold boot.

### What ATD drops: confirmed from Google's table

From [Gradle managed devices, "What's removed from ATD images?"](https://developer.android.com/studio/test/gradle-managed-devices#use-atds):
- Google apps, including Chrome and the Play Store.
- Settings apps (the Settings provider stays).
- **SystemUI**.
- AOSP apps, including **Browser2**, Camera2, Contacts and LatinIME.
- Hardware rendering is also disabled.

The page says to use `google-atd` "if you require Google Play Services", so `aosp_atd` has no Play Services.

**WebView isn't in the removal list.** I found no primary source that confirms WebView is present on ATD either. android-emulator-runner [#49](https://github.com/ReactiveCircus/android-emulator-runner/issues/49) ("No WebView installed") is from 2020, on an API 26 `default` image, and isn't about ATD.

What our tests touch, checked against `main` at `d6c8957`:

| Component | Where it's used | Needed by the `bff-tests` scope? |
| --- | --- | --- |
| Play Services / Firebase | Firebase plugin and Crashlytics in `app/build.gradle.kts:11-12,73`; the Firebase BOM in `gradle/libs.versions.toml:24-27,98-105` | **No.** The spec has UI runs skip Firebase initialisation ([#79](https://github.com/Mindera/Alfie-Android/issues/79), "UI test signal and clean start"). `:integration-test` is plain Apollo/OkHttp (`integration-test/build.gradle.kts:28-34`). |
| ML Kit barcode | `gradle/libs.versions.toml:46,132` | **No.** The scanner is out of scope in #79. |
| WebView | `feature/webview` | **No.** Web views are out of scope for UI tests in #79. |
| Custom Tabs (needs a browser) | `core/ui/.../CustomTabsUtils.kt:6-18` | **No**, as long as no journey opens an external URL. ATD has no Chrome or Browser2, so a `CustomTabsIntent` launch would find no activity to handle it. |
| SystemUI | notification shade, status bar | **No.** The spec suppresses the notification-permission prompt in UI runs. Compose tests don't use SystemUI. |

**Recommendation:** use `target: aosp_atd`. It's the smallest download, a slightly faster cold boot, and the action tests it in its own CI. If a future journey needs Play Services, change one input to `google_atd`. If a journey ever needs a real browser, move to `google_apis`.

Locally, the runner script can still use whatever AVD the developer has. ATD and `google_apis` behave the same for these tests (**estimate**).

## 2. KVM on `ubuntu-latest`, and AVD snapshot caching

### KVM: confirmed

- GitHub-hosted Linux runners "support hardware acceleration for Android SDK tools" ([GitHub-hosted runners reference](https://docs.github.com/en/actions/reference/runners/github-hosted-runners)).
- Since 2024-04-02, 2-vCPU standard runners support it too ([changelog](https://github.blog/changelog/2024-04-02-github-actions-hardware-accelerated-android-virtualization-now-available/)).
- The runner user has to be given access to `/dev/kvm`. The changelog and the [action README](https://github.com/ReactiveCircus/android-emulator-runner#running-hardware-accelerated-emulators-on-linux-runners) use this step:

  ```yaml
  - name: Enable KVM
    run: |
      echo 'KERNEL=="kvm", GROUP="kvm", MODE="0666", OPTIONS+="static_node=kvm"' | sudo tee /etc/udev/rules.d/99-kvm4all.rules
      sudo udevadm control --reload-rules
      sudo udevadm trigger --name-match=kvm
  ```

- The README recommends `ubuntu-latest` over macOS, calling it "2-3 times faster" and much cheaper.
- `ubuntu-latest` is Ubuntu 24.04 ([runner-images README](https://github.com/actions/runner-images#available-images)).

Runner size for a **public** repo, from the [GitHub-hosted runners reference](https://docs.github.com/en/actions/reference/runners/github-hosted-runners):
- `ubuntu-latest` has **4 vCPU, 16 GB RAM**. Private repos get 2 vCPU and 8 GB.
- The docs list a 14 GB SSD, but the nowinandroid log above shows `/dev/root` at 145 GB with **86 GB available**, so disk isn't the constraint (**confirmed** from one run).

What the runner image ships (runner-images [Ubuntu2404-Readme](https://github.com/actions/runner-images/blob/main/images/ubuntu/Ubuntu2404-Readme.md), image 20261101):
- platforms `android-34` and `android-37.0`, which covers `compileSdk = 37` (`buildconvention/.../AppConfig.kt:5`) and `targetSdk = 34` (`AppConfig.kt:7`);
- build-tools 34–37;
- Node 22 and Yarn 1.22.

It ships **no system images and no emulator package**. The action downloads both on every run with `sdkmanager --install …`.

Android's own requirement is that an x86_64 host runs x86 or x86_64 images, and an ARM64 host runs arm64-v8a images ([emulator acceleration](https://developer.android.com/studio/run/emulator-acceleration#development-environment-requirements)). So CI must use **x86_64**.

### AVD snapshot caching: confirmed recipe

The [README, "AVD snapshot caching"](https://github.com/ReactiveCircus/android-emulator-runner#usage--examples) gives four steps:
1. Add `gradle/actions/setup-gradle`.
2. Add `actions/cache` on `~/.android/avd/*` and `~/.android/adb*`.
3. On a cache miss, run the action once with `emulator-options` that omit `-no-snapshot`, plus `force-avd-creation: false` and `script: echo "Generated AVD snapshot for caching."`.
4. Run the tests with `-no-snapshot-save` and `force-avd-creation: false`.

The action's own CI also caches `~/.android/debug.keystore`, and its key is `avd-<api>-<os>-<target>-<arch>`.

Measured cost and benefit:
- A cache miss adds a snapshot-generation step of about **1.5–1.7 min** (download, cold boot, save) and about 8–13 s to save the cache.
- A cache hit boots in **4–10 s** instead of 25–37 s, saving about 20–30 s per run (**confirmed** from the logs above).
- The system image is under `$ANDROID_HOME`, outside `~/.android`, so it's **still downloaded on every run** even on a hit. That's 586 MB for ATD.

**Cache scoping is the trap (confirmed).** From [dependency caching](https://docs.github.com/en/actions/reference/workflows-and-actions/dependency-caching):
- Runs can restore caches from the current branch or the default branch.
- `pull_request` caches are written to `refs/pull/N/merge` and can only be restored by re-runs of that PR.
- Unused entries are evicted after **7 days**, and each repo has a **10 GB** limit.

setup-gradle's [docs](https://github.com/gradle/actions/blob/main/docs/setup-gradle.md#select-which-branches-should-write-to-the-cache):
- Its default is to **write the Gradle cache only on `main`**.
- `pull_request` and `merge_group` writes are "typically unhelpful". A merge-group cache scope is "a temporary branch … won't be available on subsequent Merge Queue executions".

The action's own CI shows the effect: every PR run in the sample logged "Cache not found" for both the AVD and Gradle caches.

So **something has to run on `push: main` and write both caches** (for example `bff-tests` itself on push, or a small cache-warm job). Otherwise every PR and merge-queue run is cold.

Today `deploy_beta.yml` is the only workflow on `push: main` (`.github/workflows/deploy_beta.yml:3-6`). It has no setup-gradle and no AVD cache. `deploy_debug.yml` has no Gradle caching at all (`.github/workflows/deploy_debug.yml:8-37`).

### Known reliability problems

All of these are open android-emulator-runner issues unless noted.

| Problem | Evidence | Mitigation |
| --- | --- | --- |
| **SDK download flake.** `sdkmanager --install` fails with "Premature EOF" or "Error on ZipFile unknown archive". | [#496](https://github.com/ReactiveCircus/android-emulator-runner/issues/496) (2026-09, repeated in AnkiDroid CI). The action's own CI hit "Premature EOF" on `android-35;google_apis` in [job 118362027372](https://github.com/ReactiveCircus/android-emulator-runner/actions/runs/39340338930/job/118362027372). | ATD's smaller download lowers the exposure (**estimate**). Re-run the failed job. The action has no retry input. |
| **Runner hangs while Gradle builds with the emulator already up.** | [#478](https://github.com/ReactiveCircus/android-emulator-runner/issues/478) (2026-06): logs stop "before logging any tests starting … Gradle is still building the APK"; it reproduced on `ubuntu-22.04` too. [#431](https://github.com/ReactiveCircus/android-emulator-runner/issues/431): "QEMU2 main loop" hangs, which commenters put down to resource pressure on free runners. | **Build every APK in a normal step _before_ the emulator step**, as the action's own CI (`assemble tests`) and home-assistant do. The emulator step should then only install the APKs and run them. |
| **Step never ends after the tests finish** (`crashpad_handler` left running). | [#385](https://github.com/ReactiveCircus/android-emulator-runner/issues/385) (35 comments; one commenter later says it "seems … fixed"). | Set a job-level `timeout-minutes`, so a hang fails in 45 min instead of 6 h (the [job limit](https://docs.github.com/en/actions/reference/limits)). |
| **`-gpu swiftshader_indirect` is deprecated** as of emulator 36.4.9 ([emulator acceleration](https://developer.android.com/studio/run/emulator-acceleration#configure-graphics-acceleration-from-the-command-line)). It's in the action's default `emulator-options`. | It still worked in the 2026-09/10 logs above. | Low risk today. If it's removed, pass `-gpu swiftshader`. |
| **The `script:` input runs each line in its own `sh -c`.** | [src/main.ts](https://github.com/ReactiveCircus/android-emulator-runner/blob/main/src/main.ts) (`parseScript`, then `exec('sh', ['-c', script])` per line). | Make `script:` a single command, `scripts/run-bff-tests.sh`, so the BFF process and the trap that tears it down live in one shell. |

## 3. Wall-clock total for `bff-tests`

### Baseline: confirmed

From `gh run list --workflow deploy_debug.yml`, the last 10 successful runs took **10.0–13.1 min**. They run on `ubuntu-latest` with no Gradle cache.

Fastlane step summary for [run 41759952844](https://github.com/Mindera/Alfie-Android/actions/runs/41759952844) (ALFMOB-407):

| Step | Time |
| --- | --- |
| `detekt` (includes Gradle bootstrap and configuration) | 127 s |
| `testDebugUnitTest` (compiles every module, then runs the unit tests) | 439 s |
| `app:assembleDebug` (after that compile) | 94 s |
| `firebase_app_distribution` | 13 s |

The commands come from `fastlane/Fastfile:19-31,71-126`.

From the [Alfie-BFF CI](https://github.com/Mindera/Alfie-BFF/actions/runs/42705860265) (**confirmed**): Node setup with yarn cache 6–12 s, `yarn install` 9–20 s, `yarn build` 6–8 s.

### Estimate for the spec's `bff-tests` steps ([#79](https://github.com/Mindera/Alfie-Android/issues/79))

| Step | Cold (no Gradle or AVD cache) | Warm (both caches written by `main`) | Basis |
| --- | --- | --- | --- |
| Job setup, two checkouts, GitHub App token | 0.5 min | 0.5 min | step timings above |
| Node setup, yarn install, yarn build | 0.5–1 min | 0.5 min | BFF CI, confirmed |
| setup-gradle restore | n/a | 0.3–0.5 min | nowinandroid: about 15–20 s |
| Gradle: `:app:assembleDebug`, `:app:assembleDebugAndroidTest`, `:integration-test:assembleDebugAndroidTest`, run **before** the emulator step | 7–10 min | 2–4 min | estimate from the 127 + 439 + 94 s baseline minus unit-test execution; the warm figure assumes a dependency and build cache hit |
| Emulator SDK install (emulator plus 586 MB ATD image) | ~1 min | ~1 min | confirmed, 34–55 s |
| AVD: generate snapshot (miss) or boot from snapshot (hit) | 1.5–2 min | 0.1–0.2 min | confirmed |
| BFF boot and `/graphql` poll | <0.5 min | <0.5 min | estimate |
| Install APKs and run `:integration-test` (3 test classes) | 1–2 min | 1–2 min | estimate |
| Run the 3–4 Compose UI journeys, with Orchestrator and `clearPackageData` | 2–4 min | 2–4 min | estimate; Orchestrator runs each test in its own instrumentation |
| Upload reports (on failure only), post-cache save | 0.5 min | 0.2 min | confirmed, 8–13 s for the AVD cache |
| **Total** | **≈ 15–21 min** | **≈ 8–12 min** | |

Against the limits:
- **45-min `timeout-minutes`:** at least 2× headroom even cold. A hang (#385, #478) would hit it rather than the 6-hour default.
- **Merge queue 60-min check timeout:** the queue waits that long "before assuming that checks have failed" ([managing a merge queue](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/configuring-pull-request-merges/managing-a-merge-queue)). A cold `bff-tests` run fits with about 40 min to spare. The job must trigger on `merge_group`, or a required check never reports and the merge fails (same page).
- **Parallelism:** `bff-tests` and `unit-tests` run as separate jobs. The Free plan allows 20 concurrent standard jobs ([limits](https://docs.github.com/en/actions/reference/limits)), so the merge queue's max-5 groups won't queue on runner capacity (**estimate**).

The Gradle number is the least certain. Measure it with a first spike run before settling on `timeout-minutes`.

## 4. GitHub Actions minutes for a public repo: confirmed

- Alfie-Android is `PUBLIC` (`gh repo view --json visibility`).
- "The use of standard GitHub-hosted runners is free … in public repositories" ([Actions billing](https://docs.github.com/en/billing/concepts/product-billing/github-actions)).
- The runners reference calls public-repo standard runner use "free and unlimited".
- "Larger runners are billed for public repositories", so stay on `ubuntu-latest`.
- Cache storage is limited to 10 GB per repo ([limits](https://docs.github.com/en/actions/reference/limits)). An ATD AVD with its snapshot plus the Gradle home should fit (**estimate**; sizes weren't measured).

For comparison only: if the repo went private, standard Linux runners cost $0.006/min against the plan's included dollar amount (same billing page). A 15-min run would be about $0.09.

## Findings that reach beyond #84

1. **#87 needs a `push: main` writer for the caches.** Without one, every PR and merge-queue `bff-tests` run is cold, about 15–21 min instead of 8–12. The options are:
   - run `bff-tests` on `push: main` as well;
   - add a cache-warm job;
   - give `deploy_beta` setup-gradle.

   Either way, the AVD cache and setup-gradle need writing from `main`. Entries also expire after 7 days of inactivity.
2. **#79's runner-script step 4 needs splitting in CI.** "Builds once and runs" inside the emulator step is the pattern linked to runner hangs (#478, #431). CI should build every APK in a plain step first. The script's Gradle invocation then only installs and runs: tasks are up to date, or the script takes a "skip build" flag. This is a change to the spec's script contract.
3. **Local AVD ABI.** The map says the script boots "an AVD (API 34, x86_64)" when no device is connected. On Apple Silicon Macs, the emulator can only VM-accelerate arm64-v8a images ([emulator acceleration requirements](https://developer.android.com/studio/run/emulator-acceleration#development-environment-requirements)). The local fallback must pick `arm64-v8a` on an arm64 host. CI stays x86_64. This is new and should be decided on the map.
4. **Image target is now a spec decision.** The spec says "a pinned API level" but no target. The recommendation is `aosp_atd`. That keeps Custom Tabs and web-view flows out of UI journeys, which matches the out-of-scope list already.
