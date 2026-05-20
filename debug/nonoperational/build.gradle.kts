import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.compose)
}

android {
    namespace = AppConfig.applicationId + ".debug.nonoperational"
}

dependencies {
    api(project(ProjectModule.debug))

    implementation(libs.chucker.noOp)
    implementation(libs.destinations.core)
}
