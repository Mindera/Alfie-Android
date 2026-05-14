import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
}

android {
    namespace = AppConfig.applicationId + ".core.configuration"
}

dependencies {
    implementation(project(ProjectModule.coreCommons))
    implementation(project(ProjectModule.coreEnvironment))
    implementation(platform(libs.firebase.bom))
    implementation(libs.moshiKotlin)
    implementation(libs.firebase.config)

    testImplementation(libs.json)
}
