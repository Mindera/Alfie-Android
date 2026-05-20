import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = AppConfig.applicationId + ".repository"
}

dependencies {
    api(project(ProjectModule.coreCommons))

    implementation(libs.paging)
    testImplementation(libs.paging.testing)
}
