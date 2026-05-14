import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

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
