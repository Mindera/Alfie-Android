import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.module.ProjectModule

plugins {
    alias(buildConvention.plugins.lib)
    alias(libs.plugins.kotlin.parcelize)
}

@Suppress("UnstableApiUsage")
android {
    namespace = AppConfig.applicationId + ".core.analytics"

    testOptions {
        unitTests {
            unitTests.isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(ProjectModule.debug))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
