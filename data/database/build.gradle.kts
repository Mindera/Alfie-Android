import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.extension.implementation
import com.mindera.alfie.buildconvention.extension.kapt
import com.mindera.alfie.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = AppConfig.applicationId + ".data.database"

    defaultConfig {
        testInstrumentationRunner = "com.mindera.alfie.data.database.AppTestRunner"
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
