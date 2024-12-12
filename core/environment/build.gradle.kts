import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
}

android {
    namespace = AppConfig.applicationId + ".core.environment"
}

dependencies {
    implementation(project(ProjectModule.dataDatastore))
    implementation(project(ProjectModule.debug))
}
