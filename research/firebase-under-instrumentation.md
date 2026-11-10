# Firebase, App Distribution and Chucker under instrumentation

Research for [#82](https://github.com/Mindera/Alfie-Android/issues/82) (Jira ALFMOB-409), blocking #85 "The UI test seam".

**Question.** In an instrumented UI run, what do Firebase, App Distribution and Chucker do on their own? How can each be turned off in a UI run without touching release behaviour?

**Snapshot.** Alfie-Android `main` @ `d6c8957`. The resolved `:app` `debugRuntimeClasspath` (`./gradlew :app:dependencies`) gives these SDK versions:

| SDK | Version |
| --- | --- |
| Firebase BoM | 32.7.3 |
| firebase-analytics / play-services-measurement | 21.5.1 |
| firebase-crashlytics | 18.6.2 (pulls firebase-sessions 1.2.1) |
| firebase-config | 21.6.2 |
| firebase-common | 21.0.0 (forced by the `firebase-common-ktx` pin) |
| firebase-appdistribution (full) | 16.0.0-beta15 |
| Chucker | 4.0.0 |

**Method.**
- Firebase docs and AndroidX/Android docs, read directly.
- `firebase-android-sdk` source on `main` (commit `dfb3d39`). For the behaviour that matters, I also checked the **exact pinned AARs** from the Gradle cache with `javap`.
- Chucker source at tag `4.0.0`.
- AOSP `ActivityThread` (android14-release).
- Each claim is marked **Confirmed** (read in source or docs) or **Inferred** (reasoned from them, not observed on a device).

---

## 0. What runs before any test code can intervene

- **Confirmed.** `FirebaseInitProvider` is merged in from `firebase-common` with `initOrder="100"`. Its `onCreate` calls `FirebaseApp.initializeApp(context)`, and the Javadoc says it is "Called before `Application#onCreate()`" ([FirebaseInitProvider.java:64-73](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-common/src/main/java/com/google/firebase/provider/FirebaseInitProvider.java#L64-L73)). Initialising the default app runs `componentRuntime.initializeEagerComponents(isDefaultApp())` ([FirebaseApp.java:606](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-common/src/main/java/com/google/firebase/FirebaseApp.java#L606)).
- **Confirmed (AOSP).** In `ActivityThread.handleBindApplication`, things happen in this order ([ActivityThread.java](https://github.com/aosp-mirror/platform_frameworks_base/blob/android14-release/core/java/android/app/ActivityThread.java#L6959-L6998)):
  1. The Application object is created (`makeApplicationInner`, L6959). This is where `AndroidJUnitRunner.newApplication` is used.
  2. Content providers are installed (L6983).
  3. `Instrumentation.onCreate` runs (L6990).
  4. `Application.onCreate` runs (L6998).

  So `FirebaseInitProvider` and every eager Firebase component run **before** the test runner's `onCreate`, before any `@Before` or rule, and before `AlfieApplication.onCreate`.
- **Confirmed (Alfie).** `AlfieApplication.onCreate` also calls `FirebaseApp.initializeApp(this.applicationContext)` ([app/src/main/java/com/mindera/alfie/AlfieApplication.kt:18](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/app/src/main/java/com/mindera/alfie/AlfieApplication.kt#L18)). When the default app already exists, `initializeApp(Context)` simply returns it ([FirebaseApp.java:234-237](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-common/src/main/java/com/google/firebase/FirebaseApp.java#L234)). The call is therefore redundant today. But it **would bring init back** if the provider were removed.
- **Inferred, and important for #85.** Suppose the UI tests use a custom runner that swaps in `HiltTestApplication`. Then `AlfieApplication.onCreate` and its `initializeApp` call do not run. `FirebaseInitProvider` still does, because the provider is declared in the app manifest and does not depend on the Application class.

**Practical consequence (Inferred).** A switch applied from test code at runtime cannot affect the *first* process launch's Firebase init. It can only:
- be persisted, and take effect from the next process start; or
- be read by app code that runs later, such as Analytics/Crashlytics calls made from Alfie code paths.

Only manifest switches, and build-time choices of which dependencies to include, act on the very first init.

## 1. Does auto-init make Analytics, Crashlytics or Remote Config network calls with no code calling them?

| SDK | Eager at init? | Network with no Alfie code? |
| --- | --- | --- |
| **Analytics** | Yes. `AnalyticsConnectorRegistrar` is `eagerInDefaultApp` (21.5.1 bytecode). | **Yes.** The docs say "Analytics automatically logs some events for you; you don't need to add any code to receive them" ([Log events](https://firebase.google.com/docs/analytics/android/events)). These are events like `first_open`, `session_start` and `screen_view`, and they are uploaded by the measurement SDK. |
| **Crashlytics** | Yes. `CrashlyticsRegistrar` is `.eagerInDefaultApp()` ([L68](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-crashlytics/src/main/java/com/google/firebase/crashlytics/CrashlyticsRegistrar.java#L68)), and so is the 18.6.2 bytecode. | **Yes, if data collection is enabled (the default).** `FirebaseCrashlytics.init` kicks off `settingsController.loadSettingsData(...)` ([FirebaseCrashlytics.java ~L163](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-crashlytics/src/main/java/com/google/firebase/crashlytics/FirebaseCrashlytics.java)). The fresh-settings fetch is chained on `dataCollectionArbiter.waitForDataCollectionPermission()` ([SettingsController.java:190-191](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-crashlytics/src/main/java/com/google/firebase/crashlytics/internal/settings/SettingsController.java#L190)), and 18.6.2 has the same call. Unsent crash reports are also sent on init when collection is on. Firebase Sessions 1.2.1, a Crashlytics dependency, is also `eagerInDefaultApp`. Bytecode shows it bails out ("No Sessions subscribers…") before `updateSettings` when no subscriber has data collection enabled (Inferred from bytecode). |
| **Remote Config** | Yes. `RemoteConfigRegistrar` is `.eagerInDefaultApp()` ([L68](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-config/src/main/java/com/google/firebase/remoteconfig/RemoteConfigRegistrar.java#L68)), and so is the 21.6.2 bytecode. | **No.** The eager work is `getDefault()` → `startLoadingConfigsFromDisk()` ([RemoteConfigComponent.java:146, 234](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-config/src/main/java/com/google/firebase/remoteconfig/RemoteConfigComponent.java#L146)), which is disk only. A fetch happens only when code calls `fetch`/`fetchAndActivate`, or adds a realtime listener. |

**Alfie specifics (Confirmed by reading code):**
- **Remote Config is dormant on `main`.** `FirebaseProvider`'s `init` calls `fetchAndActivate()` ([FirebaseProvider.kt:27-30](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/core/configuration/src/main/java/com/mindera/alfie/core/configuration/provider/FirebaseProvider.kt#L27-L30)). It is reachable only through the `FeatureHandler` / `RemoteConfigurationHandler` bindings ([FeatureConfigurationModule.kt:16-20](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/core/configuration/src/main/java/com/mindera/alfie/core/configuration/di/FeatureConfigurationModule.kt#L16-L20)), and nothing outside `:core:configuration` injects either. The SDK is still on the classpath, because `AppConventionPlugin` adds every module to `:app` ([AppConventionPlugin.kt:115](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/buildconvention/convention/src/main/kotlin/com/mindera/alfie/buildconvention/plugin/AppConventionPlugin.kt#L115)). So there is no Remote Config network traffic today. That changes the moment a screen injects a handler.
- Analytics events from Alfie code (`AnalyticsManager` in `CategoryViewModel` and `ProductDetailsViewModel`) go through `Firebase.analytics` ([AnalyticsModule.kt:22](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/core/analytics/src/main/java/com/mindera/alfie/core/analytics/di/AnalyticsModule.kt#L22)). These come on top of the automatic events.
- Firebase Installations (the FID/auth token) is pulled in lazily by whichever SDK needs it. It is not listed separately above.

## 2. Does `showFeedbackNotification` or Chucker post a notification, or ask for `POST_NOTIFICATIONS`, on API 33+?

Background (Confirmed, [Android docs](https://developer.android.com/develop/ui/views/notifications/notification-permission)):
- On API 33+, a newly installed app has notifications "off by default".
- For apps targeting 33+, the system shows the permission dialog only when the app requests it. Alfie targets 34 ([AppConfig.kt:7](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/buildconvention/convention/src/main/kotlin/com/mindera/alfie/buildconvention/AppConfig.kt#L7)).

### App Distribution `showFeedbackNotification`

- **Never requests the permission. Silently skips the notification when notifications are disabled.** Confirmed.
  - `FirebaseAppDistributionNotificationsManager.showFeedbackNotification` creates the channel (API 26+). It then returns after logging "Not showing notification because app notifications are disabled" when `NotificationManagerCompat.areNotificationsEnabled()` is false ([L158-L174](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-appdistribution/src/main/java/com/google/firebase/appdistribution/impl/FirebaseAppDistributionNotificationsManager.java#L158-L174)).
  - The beta15 bytecode has the same check and string.
  - The beta15 jar contains no permission-request activity.
  - The docs leave the request to the app: "you need to declare the permission and then request it at runtime" ([Collect feedback](https://firebase.google.com/docs/app-distribution/collect-feedback-from-testers?platform=android)).
- **If the permission *is* granted, it posts an ongoing notification.** It is hidden when the app goes to the background (same docs page).
- **The full SDK is `alwaysEager`.** Its only startup side effect is registering activity-lifecycle callbacks ([FirebaseAppDistributionRegistrar.java:68-70, 99-103](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-appdistribution/src/main/java/com/google/firebase/appdistribution/impl/FirebaseAppDistributionRegistrar.java#L68-L70)). The lifecycle handlers show dialogs only while a sign-in or update flow that code started is pending ([FirebaseAppDistributionImpl.java:492-529](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-appdistribution/src/main/java/com/google/firebase/appdistribution/impl/FirebaseAppDistributionImpl.java#L492)). Alfie never calls `checkForNewRelease`, `updateIfNewReleaseAvailable` or `signInTester`. So: no dialog and no network on its own (Confirmed by grep).
- **Alfie wiring.**
  - `FeedbackLoader` calls `showFeedbackNotification` ([FeedbackLoader.kt:12-15](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/feature/startup/src/main/java/com/mindera/alfie/feature/startup/loader/FeedbackLoader.kt#L12-L15)).
  - It runs as a `@IntoSet StartUpLoader` ([StartUpModule.kt:22](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/feature/startup/src/main/java/com/mindera/alfie/feature/startup/di/StartUpModule.kt#L22)) inside `StartUpViewModel.init` ([StartUpViewModel.kt:23-27](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/feature/startup/src/main/java/com/mindera/alfie/feature/startup/StartUpViewModel.kt#L23-L27)).

### Chucker 4.0.0

- **Posts a notification for every HTTP transaction by default, when notifications are enabled. Requests the permission only from its own UI.** Confirmed.
  - `ChuckerInterceptor(context)` builds `ChuckerCollector(context)` with `showNotification = true` ([ChuckerInterceptor.kt:38](https://github.com/ChuckerTeam/chucker/blob/4.0.0/library/src/main/kotlin/com/chuckerteam/chucker/api/ChuckerInterceptor.kt#L38), [ChuckerCollector.kt:33](https://github.com/ChuckerTeam/chucker/blob/4.0.0/library/src/main/kotlin/com/chuckerteam/chucker/api/ChuckerCollector.kt#L33)).
  - `NotificationHelper.show` posts only if `!BaseChuckerActivity.isInForeground && areNotificationsEnabled()` ([NotificationHelper.kt:87](https://github.com/ChuckerTeam/chucker/blob/4.0.0/library/src/main/kotlin/com/chuckerteam/chucker/internal/support/NotificationHelper.kt#L87)). Without the permission it is a silent no-op.
  - `POST_NOTIFICATIONS` is requested only in `MainActivity.onCreate` on API 33+ ([MainActivity.kt:98-128](https://github.com/ChuckerTeam/chucker/blob/4.0.0/library/src/main/kotlin/com/chuckerteam/chucker/internal/ui/MainActivity.kt#L98-L128)), that is, only when someone opens the Chucker UI.
  - Separately, `createShortcut` defaults to `true`, so the interceptor's `init` adds a dynamic launcher shortcut ([ChuckerInterceptor.kt:59-63, 105](https://github.com/ChuckerTeam/chucker/blob/4.0.0/library/src/main/kotlin/com/chuckerteam/chucker/api/ChuckerInterceptor.kt#L59-L63)).
- **Alfie wiring.**
  - `ChuckerCreator` uses the plain `ChuckerInterceptor(context)` ([ChuckerCreator.kt:12](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/debug/operational/src/main/java/com/mindera/alfie/debug/operational/interceptor/ChuckerCreator.kt#L12)).
  - It is added to the shared OkHttp client ([ApolloModule.kt:38-40](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/network/src/main/java/com/mindera/alfie/network/di/ApolloModule.kt#L38-L40)).
  - `:debug:operational` ships in debug, beta **and release** ([app/build.gradle.kts:65-68](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/app/build.gradle.kts#L65-L68)).

### Net effect in a UI run on API 33+

**Inferred.** Neither library raises a permission dialog by itself. Whether notifications actually appear depends on whether the run grants `POST_NOTIFICATIONS`:
- **Not granted.** Nothing is posted.
- **Granted.** This happens with `GrantPermissionRule`, `pm grant`, or an `adb install -g` style install. I did not verify whether the project's chosen invocation (Studio, `connectedAndroidTest`, Orchestrator) grants it. If it is granted, the ongoing App Distribution feedback notification and a Chucker "Recording HTTP activity" notification both appear in the shade. They don't block Compose/Espresso interaction with the app window, but they are noise in screenshots and in UI Automator flows.

**Gap to verify on the first real UI run.** Whether `POST_NOTIFICATIONS` is granted under the invocation #85 picks.

## 3. The switches, and where each can be applied

Key: **R** = can be applied at runtime, **T** = test APK / test source set only, **B** = build type / variant only (app manifest or dependencies).

**One manifest rule underlies the whole table (Confirmed).** "Instrumented tests are built into a separate APK with its own `AndroidManifest.xml`" ([Advanced test setup](https://developer.android.com/studio/test/advanced-test-setup)).

For an **application** module, that means `src/androidTest/AndroidManifest.xml` is merged into the *test* APK, not into the app under test. So meta-data or `tools:node="remove"` written there does **not** reach `FirebaseInitProvider` or the SDKs (Inferred from that statement plus merge-priority docs). To change the app's manifest only for UI runs, it has to go in a variant source set, for example `src/<buildType>/AndroidManifest.xml`, which has higher priority than `main` ([Manage manifests](https://developer.android.com/build/manage-manifests#merge_priorities)). Combine that with `testBuildType` ([Advanced test setup](https://developer.android.com/studio/test/advanced-test-setup#change-test-build-type), default `debug`).

(Library-module `androidTest` APKs are self-instrumenting, so their test manifest *does* apply to the code under test. They also lack `google_app_id`, which means `FirebaseInitProvider` logs "initialization unsuccessful" and nothing runs. This is Inferred and is relevant only if #85 tests screens from feature modules.)

| Switch | What it does | R / T / B | Notes |
| --- | --- | --- | --- |
| `<meta-data android:name="firebase_analytics_collection_enabled" android:value="false"/>` | Disables Analytics collection ([Android doc](https://firebase.google.com/docs/analytics/android/configure-data-collection)). | **B** | Read from the app manifest at measurement init (21.5.1 bytecode reads the key). Re-enable at runtime with `FirebaseAnalytics.setAnalyticsCollectionEnabled(true)`. |
| `FirebaseAnalytics.setAnalyticsCollectionEnabled(false)` | Suspends collection "until you re-enable it". | **R** | The iOS version of the same doc says the value "persists across app executions and overrides" the plist flag. The Android page doesn't say so (Inferred to be the same). It is too late for the first launch's automatic `first_open`/`session_start` (section 0). |
| `firebase_analytics_collection_deactivated = true` | Permanently deactivates Analytics in that build. It cannot be re-enabled at runtime ([doc](https://firebase.google.com/docs/analytics/android/configure-data-collection)). | **B** | The strongest Analytics off-switch. Put it in a UI-test build type's manifest. |
| `<meta-data android:name="firebase_crashlytics_collection_enabled" android:value="false"/>` | Turns off automatic Crashlytics collection ([doc](https://firebase.google.com/docs/crashlytics/android/customize-crash-reports#enable-reporting)). The settings fetch then waits for permission (section 1), reports stay local, and the Sessions subscriber reports disabled. | **B** | Precedence in `DataCollectionArbiter` is: SharedPreferences (the runtime API) > manifest > global default ([DataCollectionArbiter.java:56-79](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-crashlytics/src/main/java/com/google/firebase/crashlytics/internal/common/DataCollectionArbiter.java#L56-L79)). A device that ever ran `setCrashlyticsCollectionEnabled(true)` for this package overrides the manifest until app data is cleared. |
| `FirebaseCrashlytics.setCrashlyticsCollectionEnabled(false)` | Runtime override. It "persists across all subsequent launches", and `false` "will apply the next time the user launches the app" ([doc](https://firebase.google.com/docs/crashlytics/android/customize-crash-reports#enable-reporting)). | **R** | Next-launch semantics, so it is useless for a single fresh-install UI run. |
| `<provider android:name="com.google.firebase.provider.FirebaseInitProvider" android:authorities="${applicationId}.firebaseinitprovider" tools:node="remove"/>` | Removes auto-init. Documented only in the [Firebase blog (2017)](https://firebase.blog/posts/2017/03/take-control-of-your-firebase-init-on); `tools:node="remove"` itself is documented in [Manage manifests](https://developer.android.com/build/manage-manifests#node_markers). | **B** | **Doesn't help on its own in Alfie.** `AlfieApplication.onCreate` calls `initializeApp` anyway (section 0), so init just moves later. If init is fully suppressed, `Firebase.appDistribution` (FeedbackLoader), `Firebase.analytics` (AnalyticsModule) and `FirebaseRemoteConfig.getInstance()` throw `IllegalStateException` ("Default FirebaseApp is not initialized"). This is Inferred from the standard `getInstance` contract. All three would need replacing through DI. |
| `FirebaseApp.setDataCollectionDefaultEnabled(Boolean)` | Global default for "all SDKs unless overridden by SDK specific mechanisms" ([FirebaseApp.java:373-385](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-common/src/main/java/com/google/firebase/FirebaseApp.java#L373-L385)). Persisted in SharedPreferences ([DataCollectionConfigStorage.java:69-79](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-common/src/main/java/com/google/firebase/internal/DataCollectionConfigStorage.java#L69-L79)). | **R** (in theory) | **Not a supported public API on Android.** It is `@hide @KeepForSdk` and missing from the [public FirebaseApp reference](https://firebase.google.com/docs/reference/android/com/google/firebase/FirebaseApp). Crashlytics falls back to it, and the Analytics connector passes it to measurement (21.5.1 bytecode: `AnalyticsConnectorImpl` reads `isDataCollectionDefaultEnabled()` and subscribes to `DataCollectionDefaultChange`). Remote Config and App Distribution ignore it. Don't build on it. |
| `<meta-data android:name="firebase_data_collection_default_enabled" android:value="false"/>` | The manifest form of the global default ([DataCollectionConfigStorage.java:33-34, 81-98](https://github.com/firebase/firebase-android-sdk/blob/dfb3d395c947aa2f7e6d3d10e374a01b6ba90c37/firebase-common/src/main/java/com/google/firebase/internal/DataCollectionConfigStorage.java#L81-L98)). | **B** | Same caveat: present in source, not in Android docs. |
| **App Distribution: API-only artifact** (`firebase-appdistribution-api` without the full `firebase-appdistribution`) | "The calls will have no effect if the full SDK implementation is not present" ([Set up alerts](https://firebase.google.com/docs/app-distribution/set-up-alerts?platform=android)). | **B** | The documented off-switch. The doc also says to add the full SDK only "to variants that are intended exclusively for pre-release testing". Alfie gets the full SDK through `:debug:operational` ([debug/operational/build.gradle.kts:44-45](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/debug/operational/build.gradle.kts#L44-L45)), which is wired into **release** too. |
| **App Distribution: don't call it** (replace the `FeedbackLoader` `@IntoSet` binding in the test DI graph) | Removes the only call site. | **T** (Hilt test module) | Leaves the eager lifecycle-callback registration, which is harmless. |
| **App Distribution: `cancelFeedbackNotification()`** | Hides the notification ([doc](https://firebase.google.com/docs/app-distribution/collect-feedback-from-testers?platform=android)). | **R** | Reactive only. Not needed if the permission isn't granted. |
| **Chucker: `ChuckerCollector(context, showNotification = false)`**, passed through `ChuckerInterceptor.Builder(context).collector(...)` | No transaction notifications. `showNotification` is a public `var`, so it can also be flipped after construction ([ChuckerCollector.kt:31-35](https://github.com/ChuckerTeam/chucker/blob/4.0.0/library/src/main/kotlin/com/chuckerteam/chucker/api/ChuckerCollector.kt#L31-L35)). | **R** | `Builder.createShortcut(false)` also drops the shortcut. |
| **Chucker: `library-no-op`** | No-op interceptor. Already used by `:debug:nonoperational` ([build.gradle.kts:15](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/debug/nonoperational/build.gradle.kts#L15)). | **B** | |
| **Chucker: replace `DebugInterceptors`** in the test DI graph | No Chucker interceptor on the OkHttp client. | **T** (Hilt test module) | `DebugInterceptors` is already an interface bound in `DebugOpModule`. |
| **Notifications in general: don't grant `POST_NOTIFICATIONS`** | Both libraries become silent no-ops (section 2). | **T** (test runner / install flags) | This is the default on a fresh API 33+ install. Avoid `GrantPermissionRule` for that permission. |

### What only a build type can do

- **The manifest meta-data switches** (`firebase_analytics_collection_*`, `firebase_crashlytics_collection_enabled`, removing `FirebaseInitProvider`).
- **Removing the App Distribution full SDK.**

Doing any of these for UI runs only, without touching release, needs a variant-scoped manifest or dependency set:
- `src/debug/AndroidManifest.xml`, if UI runs keep testing `debug`; or
- a dedicated UI-test build type selected with `testBuildType`.

Release is untouched as long as nothing goes in `main`.

---

## Findings that affect other tickets / need a decision

1. **#85 (UI test seam).** Runtime switches can't reach the first init, because `FirebaseInitProvider` runs before the runner, rules and `Application.onCreate`. Three implications:
   - A UI-test seam that only swaps Hilt bindings will still have Firebase Analytics auto-events and the Crashlytics settings fetch firing against the real project from the debug APK.
   - Fully silencing them needs a **build-type-scoped manifest** (`firebase_analytics_collection_deactivated=true`, `firebase_crashlytics_collection_enabled=false`) on the variant under test.
   - **Decision needed:** put those flags in `src/debug/` (they would affect every developer's debug build) or create a dedicated UI-test build type plus `testBuildType`.
2. **#85.** If the seam uses `HiltTestApplication`, `AlfieApplication`'s `initializeApp` is skipped but auto-init still runs. Removing `FirebaseInitProvider` without replacing `Firebase.analytics`, `Firebase.appDistribution` and `FirebaseRemoteConfig.getInstance()` in DI would crash on first injection.
3. **New, outside #82's scope.** The release build ships the App Distribution **full** SDK and real Chucker, because `releaseImplementation(project(":debug:operational"))` carries a TODO ([app/build.gradle.kts:67-68](https://github.com/Mindera/Alfie-Android/blob/d6c89576dbe459d92d75e781799a6a9bfd01fe36/app/build.gradle.kts#L67-L68)). Firebase docs warn this "may result in your app being removed from Google Play". This is not a UI-test problem, but it is a release risk, and it means "don't touch release behaviour" currently preserves the wrong behaviour.
4. **Correction to the ticket's "known facts".** Remote Config is on the classpath but **never fetched on `main`**, because no consumer injects `FeatureHandler` / `RemoteConfigurationHandler`. Today it is not a UI-run concern. It becomes one, needing a Hilt fake of `RemoteConfigurationProvider`, once something injects it.
5. **Gap to verify empirically.** Whether the chosen test invocation pre-grants `POST_NOTIFICATIONS`. If it does, the App Distribution feedback notification and Chucker notifications will appear during runs. Fix that by not granting the permission, or by using the Hilt replacements above.
