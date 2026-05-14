import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
}

android {
    namespace = AppConfig.applicationId + ".core.sync"
}

dependencies {
    implementation(project(ProjectModule.coreCommons))
    implementation(libs.hilt.work)
    implementation(libs.lifecycle.viewModel)
    implementation(libs.work)
}
