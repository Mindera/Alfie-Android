import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.feature)
}

android {
    namespace = AppConfig.applicationId + ".feature.pdp"
}

dependencies {
    implementation(libs.glide)
    implementation(project(ProjectModule.coreEnvironment))
}
