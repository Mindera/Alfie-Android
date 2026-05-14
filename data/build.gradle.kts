import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

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
