import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.compose)
}

android {
    namespace = AppConfig.applicationId + ".core.ui"
}

dependencies {
    implementation(project(ProjectModule.coreCommons))

    implementation(libs.paging.compose)
    implementation(libs.androidx.browser)
    implementation(libs.compose.activity)
}
