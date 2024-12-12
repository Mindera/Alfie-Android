import au.com.alfie.ecomm.buildconvention.AppConfig

plugins {
    alias(buildConvention.plugins.feature)
}

android {
    namespace = AppConfig.applicationId + ".feature.plp"
}

dependencies {
    implementation(libs.paging.compose)
    testImplementation(libs.paging.testing)
}
