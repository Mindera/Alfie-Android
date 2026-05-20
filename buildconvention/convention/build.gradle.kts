plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(libs.android.gradleTools)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.kover.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = buildConvention.plugins.application.get().pluginId
            implementationClass = "com.mindera.alfie.buildconvention.plugin.AppConventionPlugin"
        }
        register("hilt") {
            id = buildConvention.plugins.hilt.get().pluginId
            implementationClass = "com.mindera.alfie.buildconvention.plugin.HiltConventionPlugin"
        }
        register("projectLib") {
            id = buildConvention.plugins.lib.get().pluginId
            implementationClass = "com.mindera.alfie.buildconvention.plugin.LibConventionPlugin"
        }
        register("projectComposeLib") {
            id = buildConvention.plugins.compose.get().pluginId
            implementationClass = "com.mindera.alfie.buildconvention.plugin.ComposeLibConventionPlugin"
        }
        register("projectFeature") {
            id = buildConvention.plugins.feature.get().pluginId
            implementationClass = "com.mindera.alfie.buildconvention.plugin.FeatureConventionPlugin"
        }
        register("projectDestinations") {
            id = buildConvention.plugins.destinations.get().pluginId
            implementationClass = "com.mindera.alfie.buildconvention.plugin.DestinationsConventionPlugin"
        }
        register("firebase") {
            id = buildConvention.plugins.firebase.get().pluginId
            implementationClass = "com.mindera.alfie.buildconvention.plugin.FirebaseConventionPlugin"
        }
    }
}
