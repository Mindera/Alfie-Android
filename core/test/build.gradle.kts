import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
}

android {
    namespace = AppConfig.applicationId + ".core.test"
}

dependencies {
    implementation(project(ProjectModule.coreCommons))
    implementation(project(ProjectModule.debug))

    implementation(libs.coroutines.test)
    implementation(libs.kotlin.serialization)
    implementation(libs.junit5)
}
