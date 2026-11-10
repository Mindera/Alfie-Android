# Make Gradle actually run the JUnit5 instrumented tests

Research for [#90](https://github.com/Mindera/Alfie-Android/issues/90) (Jira ALFMOB-409). Throwaway
branch, never merged. Researched 2026-11-10 against `main` @ `d6c8957`.

Each finding is marked **Verified** (run locally on this branch), **Confirmed** (read in a primary source)
or **Inferred** (reasoned, not run).

Local runs used AGP 9.4.0, Gradle 9.7.1, JDK 21 and an API 36 emulator (`emulator-5554`).

## Short answer

Bump `androidJunit5` and `junit5-instrumentation` to **2.0.2**, and delete the hardcoded `runnerBuilder`
line in `UnitTestConfig.kt`. Also add `testInstrumentationRunnerArguments["package"] = "com.mindera.alfie"`
to stop the discovery noise. With those changes, `:core:deeplink` runs 31 tests and `:data:database` runs 6,
both through Gradle. Before, both ran 0. The diff is the second commit on this branch (`Probe: plugin 2.0.2 …`).

AGP 9.4.x still drops everything after the first comma in any runner-argument value. That rules out
comma-separated values, through Gradle, for as long as the repo is on 9.4.

## Root cause (Verified)

There are two bugs, one in the plugin and one in AGP:

1. **Plugin 2.0.1** sets `configurationParameters` to `""` when the `junitPlatform.configurationParameters`
   map is empty. It then appends the unsupported-device parameter to that, so the value becomes
   `,de.mannodermaus.junit.unsupported.behavior=fail` with a leading comma.
   Sources:
   [`configure.kt@plugin-2.0.1`](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.1/plugin/android-junit5/src/main/kotlin/de/mannodermaus/gradle/plugins/junit5/internal/configure.kt)
   (`addConfigurationParameters` has no empty check, and `getAsList` splits `""` into `[""]`).
2. **AGP 9.4** joins every runner argument into one worker system property, separated by commas. The test
   engine then splits it again on `,` and `=`. The run log shows it:
   `-Dandroid-test.instrumentation-args=runnerBuilder=de.mannodermaus.junit5.AndroidJUnit5Builder,configurationParameters=,de.mannodermaus.junit.unsupported.behavior=fail`.
   That turns into an empty `configurationParameters` plus a made-up `-e de.mannodermaus.junit.unsupported.behavior fail`.
   AGP 9.4's engine also passes `-e` values to the shell unquoted, so the empty value shifts the remaining
   words. The result is `Invalid userId -2` (API 36) or `No instrumentation found` (API 33), and the task
   reports 0 tests and passes. The AGP side is described in Google issue
   [568041945](https://issuetracker.google.com/issues/568041945), which is user-filed. Its root-cause section
   cites `UtpTestUtils.kt` and `android-test-engine`'s `AndroidTestConfiguration.kt`. The comma join itself
   is visible in our own `--info` log.

## 1. Known upstream? Fixed version or workaround?

- **Known, and fixed in plugin + instrumentation 2.0.2 (Confirmed).** Upstream issue
  [#417 "AGP 9.4.1 throws error while resolving instrumented tests"](https://github.com/mannodermaus/android-junit-framework/issues/417)
  reports both of our device errors. The fix is PR
  [#415 "Avoid adding empty configParams map to prevent invalid runnerArgs"](https://github.com/mannodermaus/android-junit-framework/pull/415)
  (commit `c8d65f6`, which adds `if (values.isEmpty()) return`). It shipped in
  [2.0.2](https://github.com/mannodermaus/android-junit-framework/releases/tag/plugin-2.0.2) on 2026-10-15. The
  [instrumentation CHANGELOG](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/instrumentation/CHANGELOG.md)
  says: "Fix: Avoid adding empty configParams map to prevent invalid runnerArgs ("invalid userId -2") (#415)".
  The maintainer adds on #417 that "the latest AGP 9.5 alphas do not exhibit the problem anymore".
  Note that the upstream repo was renamed from `mannodermaus/android-junit5` to `mannodermaus/android-junit-framework`.
- **2.0.2 renames the runner builder (Confirmed, Verified).** The
  [plugin CHANGELOG](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/plugin/CHANGELOG.md)
  says the plugin now registers `de.mannodermaus.junit5.AndroidJUnitFrameworkBuilder`. `AndroidJUnit5Builder`
  is kept as a `@Deprecated` subclass
  ([`AndroidJUnitFrameworkBuilder.kt@plugin-2.0.2`](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/instrumentation/runner/src/main/kotlin/de/mannodermaus/junit5/AndroidJUnitFrameworkBuilder.kt)).
  The plugin appends its builder unless that exact name is already present, so our hardcoded line
  (`UnitTestConfig.kt:31`) produces `runnerBuilder=…AndroidJUnit5Builder,…AndroidJUnitFrameworkBuilder`.
  - On AGP 9.4 the part after the comma is silently dropped, so `am instrument` gets only
    `AndroidJUnit5Builder`. Because the deprecated alias still works, 31 tests ran (Verified).
  - That only works by accident. **Delete the line.** The plugin registers the builder itself.
    After deleting it, the run uses `-e runnerBuilder de.mannodermaus.junit5.AndroidJUnitFrameworkBuilder` (Verified).
- **No workaround on 2.0.1 (Verified, Confirmed).**
  - A non-empty `junitPlatform.configurationParameters` avoids the empty value. But it adds a comma
    (`k=v,de.mannodermaus…=fail`), which AGP 9.4 splits, so every parameter except the first becomes a bogus `-e` key.
  - Removing the argument would mean writing `testInstrumentationRunnerArguments` after the plugin does.
    The plugin writes in `whenAndroidPluginAdded`, so the ordering is fragile.
  - Neither of AGP's `android.experimental.androidTest.builtin_test_platform` and
    `android.experimental.androidTest.useUnifiedTestPlatform` flags changed the 2.0.1 outcome. All 4
    combinations still ran 0 tests (Verified).
  - The supported fix is 2.0.2.

## 2. Does the fix need a newer JUnit, AGP or Kotlin?

**No (Verified, Confirmed).**

- **AGP / Gradle.** The plugin requires at least AGP 8.2 and Gradle 8.2
  ([README@plugin-2.0.2 "Requirements"](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/README.md#requirements),
  `MIN_REQUIRED_AGP_VERSION = AndroidPluginVersion(8, 2)` in `Constants.kt`). The repo is on 9.4.0 / 9.7.1.
- **Kotlin.** 2.0.x is built with Kotlin 2.3.0 (`build-logic/gradle/libs.versions.toml@plugin-2.0.2`). The
  repo is on 2.4.20, and the 2.0.2 build compiled fine.
- **JUnit.**
  - The instrumentation libs import `junit-bom:5.14.1`, so the androidTest runtime already resolves
    `junit-jupiter-api:5.10.0 -> 5.14.1` (Verified with
    `:core:deeplink:dependencies --configuration debugAndroidTestRuntimeClasspath`).
  - Nothing needs JUnit 6. JUnit 6 would also need API 35+ devices, per the README.
  - The declared `junit5 = "5.10.0"` is misleading but harmless. Aligning it to 5.14.1 is a tidy-up, not a requirement.
- **AGP 9.5** also makes the empty-value symptom go away, according to the maintainer on #417. It isn't
  needed, and 9.5 was still alpha when #417 was discussed.

## 3. Can a 0-test instrumented run fail in Gradle itself?

**Not through any built-in option. Only through the runner script or a custom Gradle check.**

- **AGP has no such option (Confirmed).**
  - AGP 9.4's `TestOptions` DSL has no fail-on-empty switch (`javap` on `gradle-api-9.4.0.jar`).
  - None of AGP's `android.*` Gradle properties do either (`BooleanOption` / `StringOption` in `gradle-9.4.0.jar`).
  - The connected task, `DeviceProviderInstrumentTestTask`, extends `NonIncrementalTask` and not Gradle's
    `Test`. So Gradle's
    [`failOnNoDiscoveredTests`](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html)
    doesn't apply to it.
- **The plugin has no such option (Confirmed).** `InstrumentationTestOptions` exposes only `enabled`,
  `version`, `behaviorForUnsupportedDevices`, `includeExtensions` and `useConfigurationParameters`.
- **A Gradle-side guard is possible but custom (Inferred, not built).** A `doLast` on the
  `connected*AndroidTest` tasks could sum `tests=` across the result XMLs in `resultsDir` and throw on 0.
  It would depend on AGP's internal task type or on task names.
- **The runner script's "0 tests ran → fail" check stays the reliable guard.** Note that it has to accept
  modules that legitimately have no androidTest sources.

## 4. Stop the `DiscoveryIssueNotifier` "critical issue" noise

The noise isn't only slf4j (Verified, logcat for one `:core:deeplink` run). There were **20** critical
issues, for classes that can't load on ART:

- MockK JVM internals: `io.mockk.impl.log.Slf4jLogger` (needs `org.slf4j.Logger`) and
  `io.mockk.proxy.jvm.dispatcher.BootJarLoader` (needs `java.lang.instrument`)
- `kotlinx.coroutines.debug.AgentPremain` (needs `sun.misc.Signal`)
- Compose tooling classes (need `android.view.RenderNode` and `androidx.compose.animation.tooling.*`)

Why it happens:

- AndroidX's `ScanningTestLoader` hands **every class in the test APK** to the runner builder.
- The builder makes a JUnit `ClassSelector` for each one. JUnit 5.13+ (we resolve 5.14.1) then reports each
  failed class load as an ERROR-level discovery issue.
- The android-junit5 runner catches this on purpose and drops the class. The source comment in
  [`AndroidJUnitFramework.kt@plugin-2.0.2`](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/instrumentation/runner/src/main/kotlin/de/mannodermaus/junit5/internal/runners/AndroidJUnitFramework.kt)
  says so: some classes "may fail to be loaded by the class loader … Log those to console, but discard them".
  So it's noise, not a failure (Confirmed).
- Upstream [#338](https://github.com/mannodermaus/android-junit-framework/issues/338) is the same MockK
  pattern from an earlier version.

**Fix: scope the scan to our own package (Verified).** Set
`testInstrumentationRunnerArguments["package"] = AppConfig.applicationId` (`com.mindera.alfie`).

- This is AndroidJUnitRunner's
  [`package` argument](https://developer.android.com/reference/androidx/test/runner/AndroidJUnitRunner),
  and it limits which classes the scanner loads.
- With it, the same run logged **0** `DiscoveryIssue*` lines and still ran all 31 tests.
- Every androidTest source in the repo is under `com.mindera.alfie.*`.
- It's a single value with no comma, so AGP 9.4 passes it through intact.

Narrower fixes cover only part of the noise:

- Dropping the JVM `mockk` artifact from `androidTestImplementation` (keeping `mockk-android`) would fix only the MockK classes.
- Adding `slf4j-api` would fix only `Slf4jLogger`.
- JUnit's `junit.platform.discovery.issue.severity.critical` can't go above `ERROR`, so it can't silence these.

A per-run `-Pandroid.testInstrumentationRunnerArguments.package=…` overrides this default. The runner
script should know that.

## Verified runs

| Run | Plugin / runner | `runnerBuilder` in `UnitTestConfig` | `package` | Result |
| --- | --- | --- | --- | --- |
| `:core:deeplink` | 2.0.1 | `AndroidJUnit5Builder` | – | `Invalid userId -2` → `tests="0"`, BUILD SUCCESSFUL |
| `:core:deeplink` | 2.0.1 + each AGP test-platform flag `true`/`false` | same | – | `tests="0"` in all 4 runs |
| `:core:deeplink` | 2.0.2 | `AndroidJUnit5Builder` (kept) | – | `tests="31"`; AGP silently dropped the second builder; 20 discovery issues |
| `:core:deeplink` | 2.0.2 | removed | `com.mindera.alfie` | `tests="31"`, 0 discovery issues |
| `:data:database` (JUnit 4 tests, custom `AppTestRunner`) | 2.0.2 | removed | `com.mindera.alfie` | `tests="1"` + `tests="5"`, all pass |

The `useUnifiedTestPlatform=true` probe also ran on a second connected emulator (`emulator-5556`): that flag
ignored `ANDROID_SERIAL`.

## What this changes for other tickets

- **#91 (runner script contract).**
  - Keep the 0-tests guard; Gradle won't fail on its own.
  - Don't pass comma-separated runner arguments through Gradle on AGP 9.4.x
    (`-Pandroid.testInstrumentationRunnerArguments.class=A,B` runs only `A` and passes). Use one Gradle run per
    class, a single `package=`, or a single `class=`.
  - Values with shell metacharacters (for example `tests_regex` with `(`) are passed unquoted on 9.4.
  - A `package=` override replaces the default package scope.
- **#83 (test stack).**
  - JUnit 4 (`:data:database`) and JUnit 5 (`:core:deeplink`) both run under the 2.0.2 builder through Gradle.
  - The effective Jupiter version is 5.14.1, not the declared 5.10.0.
  - Adding `junitPlatform.configurationParameters` on AGP 9.4 would hit the comma bug.
- **#87 (CI shape).**
  - A CI job is only meaningful once the bump has landed, and it still needs the script's 0-tests guard.
  - Upstream's own CI moved from Firebase Test Lab to emulator.wtf (PR #418). That's not a requirement for us.
