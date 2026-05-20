import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = AppConfig.applicationId + ".core.commons"
}

dependencies {
    implementation(project(ProjectModule.debug))
    implementation(libs.moshiKotlin)
}
