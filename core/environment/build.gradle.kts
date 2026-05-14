import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

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
