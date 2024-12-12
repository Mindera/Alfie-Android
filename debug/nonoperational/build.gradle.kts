import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.module.ProjectModule

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
