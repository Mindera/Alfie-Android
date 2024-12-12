import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.extension.testImplementation
import au.com.alfie.ecomm.buildconvention.module.FeatureModule
import au.com.alfie.ecomm.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.compose)
    id(buildConvention.plugins.destinations.get().pluginId)
}

android {
    namespace = AppConfig.applicationId + ".debug.operational"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "GIT_BRANCH", "\"${getGitDetails("--abbrev-ref")}\"")
        buildConfigField("String", "GIT_COMMIT", "\"${getGitDetails("--short")}\"")
    }
}

dependencies {
    api(project(ProjectModule.debug))

    implementation(project(ProjectModule.designSystem))
    implementation(project(ProjectModule.coreCommons))
    implementation(project(ProjectModule.coreDeeplink))
    implementation(project(ProjectModule.coreEnvironment))
    implementation(project(ProjectModule.coreNavigation))
    implementation(project(ProjectModule.coreUi))
    implementation(project(FeatureModule.featureSearch))
    implementation(project(ProjectModule.repository))
    implementation(project(ProjectModule.domain))

    testImplementation(libs.junit5.params)

    implementation(libs.chucker)
    implementation(libs.destinations.core.animations)
    implementation(libs.hilt.navigation)
    implementation(libs.lifecycle.runtimeCompose)
    implementation(libs.glide)
    // WARNING: never put this dependency on a production build, it can cause the removal of the app from the store by Google Play
    implementation(libs.firebase.app.distribution.full)
}

fun getGitDetails(command: String): String = providers.exec {
    commandLine("git", "rev-parse", command, "HEAD")
}.standardOutput.asText.get().trim()
