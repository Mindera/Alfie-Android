import com.mindera.alfie.buildconvention.BuildType.BETA
import com.mindera.alfie.buildconvention.BuildType.DEBUG
import com.mindera.alfie.buildconvention.BuildType.RELEASE
import com.mindera.alfie.buildconvention.BuildType.UI_TEST
import com.mindera.alfie.buildconvention.Environment
import com.mindera.alfie.buildconvention.extension.betaImplementation
import com.mindera.alfie.buildconvention.extension.uiTestImplementation
import com.mindera.alfie.buildconvention.module.ProjectModule
import com.mindera.alfie.buildconvention.setEnvironmentsFields

plugins {
    alias(buildConvention.plugins.application)
    alias(buildConvention.plugins.firebase)
    alias(libs.plugins.firebase.crashlytics)
}

val versionNameConfig = rootProject.extra["versionNameConfig"] as String
val versionCodeConfig = rootProject.extra["versionCodeConfig"] as Int

android {
    defaultConfig {
        versionCode = versionCodeConfig
        versionName = findProperty("versionName") as? String? ?: versionNameConfig

        manifestPlaceholders["prod_host"] = Environment.Prod.webHost
        setEnvironmentsFields()

        // Only the UI test build skips the startup screen and its loaders.
        buildConfigField("boolean", "SKIP_STARTUP_SCREEN", "false")

        // Android Test Orchestrator runs every test in its own instrumentation and clears the app's
        // data first: no Room, DataStore, Glide cache or Firebase state carries over between tests.
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }

    // The UI journeys run against the UI test build (see GLOSSARY.md), not debug.
    testBuildType = UI_TEST.buildName

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    buildFeatures {
        buildConfig = true
    }

    signingConfigs {
        create("release") {
            keyAlias = findProperty("keyAlias") as String
            keyPassword = findProperty("keyPassword") as String
            storeFile = file(findProperty("storeFile") as String)
            storePassword = findProperty("storePassword") as String
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = DEBUG.applicationIdSuffix
        }

        create(BETA.buildName) {
            initWith(getByName(DEBUG.buildName))
            signingConfig = signingConfigs.getByName(RELEASE.buildName)
            matchingFallbacks.add(RELEASE.buildName)

            isMinifyEnabled = false
            applicationIdSuffix = BETA.applicationIdSuffix
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        // The UI test build: debug-derived and never distributed. src/uiTest/AndroidManifest.xml
        // switches Analytics and Crashlytics collection off from the first launch.
        create(UI_TEST.buildName) {
            initWith(getByName(DEBUG.buildName))
            matchingFallbacks.add(DEBUG.buildName)
            buildConfigField("boolean", "SKIP_STARTUP_SCREEN", "true")
        }

        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName(RELEASE.buildName)
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    debugImplementation(project(ProjectModule.debugOperational))
    betaImplementation(project(ProjectModule.debugOperational))
    // The same operational debug module as debug, so the environment defaults to DEV and the Mock BFF.
    // :debug:nonoperational would switch the app to Prod.
    uiTestImplementation(project(ProjectModule.debugOperational))
    // TODO: replace with debugNonOperational soon once we have enough working features
    releaseImplementation(project(ProjectModule.debugOperational))

    implementation(libs.androidx.splash)
    implementation(libs.destinations.core.animations)
    implementation(libs.kotlin.collectionsImmutable)
    implementation(libs.firebase.crashlytics)

    // UI journeys: JUnit4 + Compose's v1 test API, on the real AlfieApplication and Hilt graph.
    androidTestImplementation(project(ProjectModule.coreTestSeed))
    androidTestImplementation(platform(libs.compose))
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.test.core.ktx)
    androidTestImplementation(libs.test.runner)
    androidTestUtil(libs.test.orchestrator)
    androidTestUtil(libs.test.services)
}

// The android-junit5 plugin adds its JUnit 5 Compose extension to any module that uses Compose. That
// pulls in ui-test 1.10, which needs a newer Compose UI than the app's 1.7.7 (NoClassDefFoundError on a
// device). The journeys are JUnit4 on Compose's own test rule, so they don't need it.
configurations.matching { it.name.contains("AndroidTest") }.configureEach {
    exclude(group = "de.mannodermaus.junit5", module = "android-test-compose")
}
