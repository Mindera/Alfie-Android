import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.extension.implementation
import au.com.alfie.ecomm.buildconvention.extension.kapt
import au.com.alfie.ecomm.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = AppConfig.applicationId + ".data.database"

    defaultConfig {
        testInstrumentationRunner = "au.com.alfie.ecomm.data.database.AppTestRunner"
    }
}

dependencies {
    implementation(libs.room)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.hilt.androidTesting)
    androidTestImplementation(libs.test.core.ktx)
    androidTestImplementation(libs.test.runner)
}
