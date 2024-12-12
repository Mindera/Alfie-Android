import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = AppConfig.applicationId + ".data"
}

dependencies {
    implementation(project(ProjectModule.network))
    implementation(project(ProjectModule.repository))
    implementation(project(ProjectModule.dataDatabase))
    implementation(project(ProjectModule.dataDatastore))
}
