import com.mindera.alfie.buildconvention.AppConfig

plugins {
    alias(buildConvention.plugins.lib)
}

android {
    namespace = AppConfig.applicationId + ".core.deeplink"
}

dependencies {
    implementation(libs.destinations.core)
}
