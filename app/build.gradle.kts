import com.mindera.alfie.buildconvention.BuildType.BETA
import com.mindera.alfie.buildconvention.BuildType.DEBUG
import com.mindera.alfie.buildconvention.BuildType.RELEASE
import com.mindera.alfie.buildconvention.Environment
import com.mindera.alfie.buildconvention.extension.betaImplementation
import com.mindera.alfie.buildconvention.module.ProjectModule
import com.mindera.alfie.buildconvention.setEnvironmentsFields

plugins {
    alias(buildConvention.plugins.application)
    alias(buildConvention.plugins.firebase)
    alias(libs.plugins.firebase.crashlytics)
}

val versionNameConfig: String by rootProject.extra
val versionCodeConfig: Int by rootProject.extra

android {
    defaultConfig {
        versionCode = versionCodeConfig
        versionName = findProperty("versionName") as? String? ?: versionNameConfig

        manifestPlaceholders["prod_host"] = Environment.Prod.webHost
        setEnvironmentsFields()
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
    // TODO: replace with debugNonOperational soon once we have enough working features
    releaseImplementation(project(ProjectModule.debugOperational))

    implementation(libs.androidx.splash)
    implementation(libs.destinations.core.animations)
    implementation(libs.kotlin.collectionsImmutable)
    implementation(libs.firebase.crashlytics)
}
