import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

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
