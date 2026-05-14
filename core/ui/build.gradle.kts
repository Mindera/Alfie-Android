import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

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
