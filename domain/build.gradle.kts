import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = AppConfig.applicationId + ".domain"
}

dependencies {
    api(project(ProjectModule.repository))
    api(project(ProjectModule.network))

    implementation(libs.paging)
    testImplementation(libs.paging.testing)
}
