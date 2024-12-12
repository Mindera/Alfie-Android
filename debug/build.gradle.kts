import au.com.alfie.ecomm.buildconvention.AppConfig

plugins {
    alias(buildConvention.plugins.compose)
}

android {
    namespace = AppConfig.applicationId + ".debug"
}

dependencies {
    implementation(libs.apollo)
    implementation(libs.destinations.core)
}
