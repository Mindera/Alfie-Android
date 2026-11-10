# Test stack for UI journeys: JUnit5, Compose UI test, Hilt, Orchestrator

Research for [#83](https://github.com/Mindera/Alfie-Android/issues/83) (Jira ALFMOB-409). Throwaway
branch, never merged. Researched 2026-11-10 against `main` @ `d6c8957`.

Each finding is marked **Confirmed** (read in a primary source or in this repo) or **Inferred**
(a reasoned conclusion that has not been built or run). Nothing here was compiled or run on a device.

## What the repo uses today

| Thing | Value | Where |
| --- | --- | --- |
| AGP / Gradle / Kotlin | 9.4.0 / 9.7.1 / 2.4.20 | `gradle/libs.versions.toml:3,38`, `gradle/wrapper/gradle-wrapper.properties` |
| android-junit5 Gradle plugin | 2.0.1 (`de.mannodermaus.android-junit5`) | `libs.versions.toml:4,156` |
| Instrumentation runner lib | `de.mannodermaus.junit5:android-test-runner:2.0.1` | `libs.versions.toml:37,118` |
| JUnit Jupiter (declared) | 5.10.0, but the 2.0.1 instrumentation libs import `junit-bom:5.14.1` as a Gradle platform, so the androidTest runtime resolves to 5.14.1 | `libs.versions.toml:36`; [`android-test-core-2.0.1.module`](https://repo1.maven.org/maven2/de/mannodermaus/junit5/android-test-core/2.0.1/android-test-core-2.0.1.module) |
| Compose | BOM `2025.01.01` = **Compose UI 1.7.7** | `libs.versions.toml:12`; [BOM pom](https://dl.google.com/android/maven2/androidx/compose/compose-bom/2025.01.01/compose-bom-2025.01.01.pom) |
| Hilt | 2.60.1 | `libs.versions.toml:31` |
| AndroidX Test | runner 1.6.2, ext-junit 1.2.1, espresso 3.6.1; no orchestrator, no `ui-test-*` | `libs.versions.toml:6,23,58` |
| minSdk / targetSdk | 29 / 34 | `buildconvention/.../AppConfig.kt:5-7` |

How the build is wired:

- Every app and lib module gets `testInstrumentationRunner = androidx.test.runner.AndroidJUnitRunner` plus
  `runnerBuilder = de.mannodermaus.junit5.AndroidJUnit5Builder`
  (`buildconvention/.../plugin/configuration/UnitTestConfig.kt:29-32`, `AppConfig.kt:8`). Its
  androidTest dependencies are Jupiter API, params, runner, MockK, Turbine and `:core:test` (`UnitTestConfig.kt:48-56`).
- `alfie.application` applies `android-junit5` and `alfie.hilt` (`AppConventionPlugin.kt:45,48`).
  `alfie.hilt` adds only `hilt-android` + `ksp(hilt-compiler)`; there is no `hilt-android-testing` or
  `kspAndroidTest` (`HiltConventionPlugin.kt:29-32`).
- `:data:database` swaps in `HiltTestApplication` through `AppTestRunner` (`data/database/src/androidTest/.../AppTestRunner.kt:8-11`,
  `data/database/build.gradle.kts:15,26`). **No test in the repo uses `@HiltAndroidTest` or `HiltAndroidRule`**, so that runner is set up but nothing uses it yet.
- The `:data:database` tests are **JUnit 4** (`org.junit.Test`) and are configured to run under the same runner with the JUnit5 builder
  (`PersistentDatabaseMigrationsTest.kt:13`, `RecentSearchDaoTest.kt:15-18`). So the source already mixes JUnit 4 and 5. Through Gradle, though,
  **nothing runs today**; see the next section.

## Through Gradle, instrumented tests run 0 tests today (added after first publication)

Reported by the coordinator, evidence in [#81 comment](https://github.com/Mindera/Alfie-Android/issues/81#issuecomment-6757890457) (**Confirmed** there, not re-run here).
With plugin 2.0.1, `./gradlew <module>:connectedDebugAndroidTest` runs **0 tests and reports success**. The plugin adds an empty
`-e configurationParameters` argument, and `am instrument` then fails on the device ("Invalid userId -2" on API 36, "No instrumentation found" on API 33).
Running the same APK directly with `adb shell am instrument -e runnerBuilder …AndroidJUnit5Builder` discovers and runs the JUnit5 tests.
The fix is ticket #90 and is not covered here. Plugin 2.0.2's changelog lists a matching fix
("Avoid adding empty configParams map to prevent invalid runnerArgs ('invalid userId -2') (#415)",
[CHANGELOG](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/instrumentation/CHANGELOG.md)).

What this means for the JUnit5-vs-JUnit4 comparison:

- **The bug hits the whole module, not just JUnit5 tests (Confirmed from source).** Plugin 2.0.1 writes the `runnerBuilder` and
  `configurationParameters` arguments into `defaultConfig` for the whole module whenever the plugin is applied and Jupiter is on
  `androidTestImplementation` ([`configure.kt@plugin-2.0.1`](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.1/plugin/android-junit5/src/main/kotlin/de/mannodermaus/gradle/plugins/junit5/internal/configure.kt),
  `prepareInstrumentationTests`). Alfie's convention puts Jupiter on every module's `androidTestImplementation` (`UnitTestConfig.kt:48`).
  So JUnit4 journeys in `:app` are blocked exactly like JUnit5 ones (**Inferred**: the whole `am instrument` invocation fails).
  Inside `:app` the bug therefore does not favour either framework. Either way #90 has to land first.
- **No passing Gradle run backs either framework yet.** The claim that "the repo already runs JUnit4 next to JUnit5 on-device" holds only at the
  source level. The direct-`adb` run proves the JUnit5 path. Whether the JUnit4 `:data:database` tests pass under the builder is not shown.
- **It is another data point on how fragile the plugin is.** The JUnit5 route depends on the android-junit5 plugin's instrumentation wiring, and
  this is the second plugin defect on our critical path (after #395 for `com.android.test`). A JUnit4-only journey setup can skip the plugin's
  instrumentation wiring entirely. The plugin returns early when Jupiter is absent from `androidTestImplementation`, or the plugin
  can simply not be applied. That makes the setup immune to this class of bug (**Inferred**).
  For layout this slightly favours keeping journeys out of the plugin's reach. That is a point *for* a JUnit4 `com.android.test` module, or for
  removing Jupiter from `:app`'s androidTest, and it partly offsets the §3 leaning.
  Side finding: `RecentSearchDaoTest` puts the Jupiter `@ExtendWith(CoroutineExtension::class)` on a JUnit 4 class (`RecentSearchDaoTest.kt:22`).
  JUnit 4 ignores Jupiter extensions, so that extension most likely never runs (**Inferred**).
- `:integration-test` is a `com.android.library` with only an `androidTest` source set, so its test APK instruments itself
  (`integration-test/build.gradle.kts:5-7,32-33`).
- CI runs `connectedDebugAndroidTest` from fastlane (`fastlane/Fastfile:130`).

## 1. Can JUnit5 drive Compose against a launched `MainActivity` with Hilt? How mature is it next to JUnit4?

### Compose + launched activity: yes (Confirmed)

The android-junit5 project (now named **android-junit-framework**, [repo](https://github.com/mannodermaus/android-junit-framework))
ships `android-test-compose`, which includes `createAndroidComposeExtension<A : ComponentActivity>()` and an overload
that takes a custom `ActivityScenario` supplier
([`AndroidComposeExtension.kt`](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/instrumentation/compose/src/main/java/de/mannodermaus/junit5/compose/AndroidComposeExtension.kt)).
The project's own test drives an existing activity this way
([`ExistingActivityComposeExtensionTests.kt`](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/instrumentation/compose/src/androidTest/java/de/mannodermaus/junit5/compose/ExistingActivityComposeExtensionTests.kt)).
The plugin adds the compose artifact on its own when any `androidx.compose*` dependency is on `androidTestImplementation`
([`configure.kt`](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/plugin/android-junit5/src/main/kotlin/de/mannodermaus/gradle/plugins/junit5/internal/configure.kt), `attachDependencies`; [README "Jetpack Compose"](https://github.com/mannodermaus/android-junit-framework#jetpack-compose)).

How it differs from the JUnit4 rule (**Confirmed** from source):

- It is a different API from `createAndroidComposeRule`. Everything runs inside one `extension.use { … }` block per test, and only one `use` call is allowed per `@Test`.
- The activity is launched **lazily, inside `use {}`**, during the test body. `ActivityScenarioRule` launches it before `@Before`.
- It is `@ExperimentalTestApi` and is built on Compose's `AndroidComposeUiTestEnvironment` from the **v1** `androidx.compose.ui.test` package.

### Hilt: no first-party JUnit5 support (Confirmed); a custom adapter looks feasible (Inferred)

- `HiltAndroidRule` is a JUnit 4 `TestRule`, and Hilt says it "is required. The Dagger component will not be created without this test rule"
  ([`HiltAndroidRule.java`](https://github.com/google/dagger/blob/master/hilt-android-testing/main/java/dagger/hilt/android/testing/HiltAndroidRule.java)).
  The rule does its work in `Statement.evaluate()`: it sets the test instance, marks that the rule ran, and clears state afterwards
  ([`MarkThatRulesRanRule.java`](https://github.com/google/dagger/blob/master/hilt-android-testing/main/java/dagger/hilt/android/internal/testing/MarkThatRulesRanRule.java)).
- Neither the Hilt docs ([dagger.dev](https://dagger.dev/hilt/testing.html), [developer.android.com](https://developer.android.com/training/dependency-injection/hilt-testing))
  nor the google/dagger issue tracker mention JUnit 5 or Jupiter. Both docs show the JUnit 4 setup `@get:Rule(order = 0) HiltAndroidRule` followed by
  `@get:Rule(order = 1) createAndroidComposeRule<…>()`.
- To use Hilt with JUnit5 you would write a Jupiter extension, for example an `InvocationInterceptor` that wraps each test method in
  `HiltAndroidRule(testInstance).apply(statement, Description)`. That depends on `MarkThatRulesRanRule` internals, which are not a published contract.
  The lazy activity launch in `AndroidComposeExtension` helps here: by the time `use {}` launches `MainActivity`, the wrapper has already
  created the component. **Inferred, not built**; this needs a spike.

### Maturity next to JUnit4 (Confirmed facts; the overall weighing is Inferred)

| | JUnit4 (`ui-test-junit4` + `HiltAndroidRule`) | JUnit5 (`android-test-compose` + custom Hilt adapter) |
| --- | --- | --- |
| Owner | Google (Compose, Hilt, AndroidX Test) | One maintainer ([README "Official Support"](https://github.com/mannodermaus/android-junit-framework#official-support): Google has no plans for JUnit 5+) |
| Hilt docs | Documented, including rule ordering | None. You write and maintain the adapter yourself |
| Compose API | `createAndroidComposeRule`. Since Compose 1.11 the v1 rule is deprecated in favour of `…junit4.v2.createAndroidComposeRule`, which uses `StandardTestDispatcher` ([ref](https://developer.android.com/reference/kotlin/androidx/compose/ui/test/junit4/package-summary), [release notes 1.11.0-alpha03](https://developer.android.com/jetpack/androidx/releases/compose-ui)) | `@ExperimentalTestApi`, wraps the **v1** environment, and duplicates the rule's methods in `ComposeContext` because the base interface is sealed ([#353](https://github.com/mannodermaus/android-junit-framework/issues/353)) |
| Compose version coupling | Follows the app's BOM | Each release pins a Compose version: 2.0.x → **Compose 1.10.0**, 1.9.0 → 1.7.8 ([2.0.1 pom](https://repo1.maven.org/maven2/de/mannodermaus/junit5/android-test-compose/2.0.1/android-test-compose-2.0.1.pom)). Updates lag the Compose release ([#353](https://github.com/mannodermaus/android-junit-framework/issues/353)) |
| Open bugs touching UI tests | n/a | [#368](https://github.com/mannodermaus/android-junit-framework/issues/368) (`use {}` block not executed), [#382](https://github.com/mannodermaus/android-junit-framework/issues/382) (runTest timeout with Compose 1.9), [#410](https://github.com/mannodermaus/android-junit-framework/issues/410) (parameterized-test IDs with external runners) |

**Effect on Alfie (Inferred, high confidence):** the app is on Compose UI 1.7.7. Adding `android-test-compose:2.0.x` to `:app`
androidTest would ask for Compose 1.10.0. Android's docs say the app's runtime classpath "determines the version numbers that Gradle
requires for matching dependencies in the runtime classpath for the app's test APK", and AGP does not downgrade
([dependency-resolution-errors](https://developer.android.com/build/dependency-resolution-errors#fix_conflicts_between_classpaths)).
So the JUnit5 Compose path most likely forces a **Compose BOM bump to ≥ 1.10** or pinning an older instrumentation version. The
JUnit4 `ui-test-junit4` from the existing BOM needs neither.

**Leaning from the facts:** JUnit4 for UI journeys (Compose rule + `HiltAndroidRule`) is the path the Hilt and Compose docs describe, and
the repo's runner is configured to execute JUnit 4 classes next to JUnit 5 ones (not yet proven through Gradle, see above). JUnit5 for
journeys would mean a homemade Hilt adapter, an experimental Compose wrapper, a forced Compose upgrade, and a hard dependency on the
android-junit5 plugin's instrumentation wiring, which is currently broken (#90).

## 2. Does Android Test Orchestrator with `clearPackageData` work with the android-junit5 runner builder?

**Yes, supported since instrumentation 1.5.0 (2024) (Confirmed).**

- The runner detects the Orchestrator (`orchestratorService` argument) and switches to legacy test-name formatting so the
  Orchestrator can rerun each test by `class#method`
  ([`JUnitFrameworkRunnerParams.kt`](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/instrumentation/runner/src/main/kotlin/de/mannodermaus/junit5/internal/runners/JUnitFrameworkRunnerParams.kt),
  [`AndroidJUnitFramework.kt`](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/instrumentation/runner/src/main/kotlin/de/mannodermaus/junit5/internal/runners/AndroidJUnitFramework.kt);
  changelog 1.5.0 "Improve integration with Android Test Orchestrator and remove the need for `@UseTechnicalNames` (#337)"
  ([CHANGELOG](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/instrumentation/CHANGELOG.md), [#337](https://github.com/mannodermaus/android-junit-framework/issues/337)).
  The wiki page that still asks for `@UseTechnicalNames` is outdated, and the maintainer says so in #337.
- Caveats (**Confirmed**):
  - Orchestrator plus JUnit5 **parallel execution** throws at discovery time.
  - Under the Orchestrator, test names use the legacy format, so parameterized names look "ugly" (#337).
- `clearPackageData` is a plain Orchestrator argument. After each test the Orchestrator runs `pm clear` on the app under test
  ([AndroidJUnitRunner docs, "Use Android Test Orchestrator"](https://developer.android.com/training/testing/instrumented-tests/androidx-test-libraries/runner#use-android)).
  It never reaches the runner builder, and nothing in android-junit-framework references it (code search: no hits). The cost is extra run time, because the
  app restarts for every test (same doc).
- Setup: `testOptions.execution = "ANDROIDX_TEST_ORCHESTRATOR"`, `androidTestUtil("androidx.test:orchestrator")` (latest stable **1.6.1**),
  and `testInstrumentationRunnerArguments["clearPackageData"] = "true"` (same doc; versions from [maven.google.com](https://dl.google.com/android/maven2/androidx/test/orchestrator/maven-metadata.xml)).
- JUnit 4 tests under the Orchestrator are the first-party case (**Confirmed**: same doc).
- **Not verified on a device:** Orchestrator + `clearPackageData` + `HiltTestApplication` + this repo's runner. Each test gets a fresh
  process, and Hilt clears its state per test anyway (`MarkThatRulesRanRule` `clearState()`), so no conflict is expected (**Inferred**).

## 3. `app/src/androidTest` versus a separate `com.android.test` module

| Concern | `app/src/androidTest` | Separate `com.android.test` module targeting `:app` |
| --- | --- | --- |
| android-junit5 plugin | Already applied to `:app` and wires the runner builder and deps (`AppConventionPlugin.kt:45`, `UnitTestConfig.kt:29-56`) | **The plugin breaks configuration on `com.android.test`**: "Configuration with name 'testImplementation' not found", open since plugin 1.13.2.0 and still reported on the latest version ([#395](https://github.com/mannodermaus/android-junit-framework/issues/395)). The maintainer's workaround is to not apply the plugin and wire the runner builder and `android-test-core/runner/compose` deps by hand. Support for `com.android.test` was removed in 2018 ([PR #134](https://github.com/mannodermaus/android-junit-framework/pull/134)). Alfie would need a new convention plugin that skips `android-junit5`. (**Confirmed**) |
| JUnit4 instead | Configured under the same runner (`:data:database`), but blocked by the empty-`configurationParameters` bug like everything else in the module until #90 lands | Works. A JUnit4-only module needs no android-junit5 at all (**Inferred**) |
| Hilt test app | Standard documented setup: a custom runner returning `HiltTestApplication`, plus `androidTestImplementation(hilt-android-testing)` + `kspAndroidTest(hilt-compiler)` ([docs](https://developer.android.com/training/dependency-injection/hilt-testing)). `MainActivity` is already `@AndroidEntryPoint` (`MainActivity.kt:27`), so no `HiltTestActivity` is needed for journeys | The Hilt Gradle plugin supports test modules: `TestAndroidComponentsExtension` maps to project type `TEST` and the aggregating task marks it as a test environment ([`HiltGradlePlugin.kt`](https://github.com/google/dagger/blob/master/java/dagger/hilt/android/plugin/main/src/main/kotlin/dagger/hilt/android/plugin/HiltGradlePlugin.kt), [`Variants.kt`](https://github.com/google/dagger/blob/master/java/dagger/hilt/android/plugin/main/src/main/kotlin/dagger/hilt/android/plugin/util/Variants.kt)). It has a regression history: [#3478](https://github.com/google/dagger/issues/3478) (aggregation failed with more than one test; fixed 2022). Tests and deps go in `src/main` with `implementation`/`ksp`. Neither Hilt doc page mentions this setup (**Confirmed**). Whether Hilt's aggregation sees every production `@InstallIn` module through a test module's classpath is unverified (**Inferred** risk; needs a spike) |
| Build time | androidTest compiles as part of `:app`. Hilt generates test components over the whole app graph either way (**Inferred**) | Isolation is the reason AGP gives ("to isolate the rest of your code from your tests", [advanced-test-setup](https://developer.android.com/studio/test/advanced-test-setup#use-separate-test-modules-for-instrumented-tests)). The work is the same: the app APK, the test APK and Hilt test components all still build. Test-only edits don't invalidate `:app` tasks in either layout, because androidTest is its own compilation. No primary source quantifies a difference (**Inferred: small**) |
| Variants | `testBuildType` (debug by default). Uses the app's `debug`/`beta`/`release` types directly | Only `debug` by default. Other build types need `matchingFallbacks`/variant filters (same AGP doc). `:integration-test` already does this dance (`integration-test/build.gradle.kts:12-25`) |
| Shared test-support module (seed, shared with `:integration-test`) | `androidTestImplementation(project(":test-support"))` | `implementation(project(":test-support"))` |

Both layouts can share a `com.android.library` test-support module with `:integration-test`, which consumes it as
`androidTestImplementation` (**Inferred**, standard Gradle). Watch that the support module does not pull in a dependency
version above the app's: the classpath rule quoted in §1 applies to the `:app` test APK.

**Leaning from the facts:** `app/src/androidTest` is the lower-friction path. It reuses the plugin and Hilt wiring the repo already has,
and it avoids android-junit5 #395 and the less-documented Hilt + `com.android.test` combination. A separate module is viable for
JUnit4 journeys, but it needs its own convention plugin and a Hilt aggregation spike. Counterweight: a module outside the plugin's
instrumentation wiring is immune to plugin bugs like the current 0-tests one (#90).

## Things this surfaces to decide (not decided here)

1. **JUnit4 or JUnit5 for journeys.** JUnit5 for journeys implies a Compose BOM bump to ≥ 1.10 and a homemade Hilt extension.
   JUnit4 implies the repo officially runs mixed JUnit 4/5 on-device, as `:data:database` already does in practice.
2. **Compose v1 or v2 test APIs.** On Compose ≥ 1.11, the v1 `createAndroidComposeRule` is deprecated and v2 defaults to
   `StandardTestDispatcher`. A future BOM bump will touch any journey test written against v1.
3. **`AppTestRunner` location.** The Hilt runner lives in `:data:database` androidTest and nothing uses it. `:app` (or a test module) needs its own,
   or it moves into a shared test-support module.
4. **runnerBuilder string.** The repo hardcodes the deprecated alias `AndroidJUnit5Builder` (`UnitTestConfig.kt:31`). Plugin 2.0.2
   switched its own constant to `AndroidJUnitFrameworkBuilder` and appends it when that exact name is missing
   ([plugin CHANGELOG 2.0.2](https://github.com/mannodermaus/android-junit-framework/blob/plugin-2.0.2/plugin/CHANGELOG.md), `configure.kt`).
   After a bump both names would be in the list (**Inferred**: harmless or double discovery; check when bumping).
   The manual `junit5-runner` dependency is also redundant, because the plugin adds core + runner itself.
