import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.feature)
}

android {
    namespace = AppConfig.applicationId + ".feature.startup"
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(project(ProjectModule.coreSync))
    implementation(libs.firebase.app.distribution.api)
}
