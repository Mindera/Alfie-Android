import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.compose)
    alias(buildConvention.plugins.feature) apply false
}

android {
    namespace = AppConfig.applicationId + ".feature"
}

dependencies {
    implementation(project(ProjectModule.coreAnalytics))
    implementation(project(ProjectModule.coreCommons))
    implementation(project(ProjectModule.coreDeeplink))
    implementation(project(ProjectModule.coreNavigation))
    implementation(project(ProjectModule.coreUi))
    implementation(project(ProjectModule.designSystem))
    implementation(project(ProjectModule.domain))

    implementation(libs.destinations.core)
    implementation(libs.hilt.navigation)
}
