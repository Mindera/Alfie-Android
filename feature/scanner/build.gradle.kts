import com.mindera.alfie.buildconvention.AppConfig

plugins {
    alias(buildConvention.plugins.feature)
}

android {
    namespace = AppConfig.applicationId + ".feature.scanner"
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.mlkit.barcodeScanning)
}
