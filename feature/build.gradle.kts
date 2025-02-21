import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.module.ProjectModule
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(buildConvention.plugins.compose)
    alias(buildConvention.plugins.feature) apply false
}

android {
    namespace = AppConfig.applicationId + ".feature"
}

dependencies {
    implementation(project(ProjectModule.coreCommons))
    implementation(project(ProjectModule.coreDeeplink))
    implementation(project(ProjectModule.coreNavigation))
    implementation(project(ProjectModule.coreUi))
    implementation(project(ProjectModule.designSystem))
    implementation(project(ProjectModule.domain))

    implementation(libs.destinations.core)
    implementation(libs.hilt.navigation)
}

subprojects {
    tasks.withType<KotlinCompile>().all {
        kotlinOptions.freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                    project.buildDir.absolutePath + "/compose_metrics",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                    project.buildDir.absolutePath + "/compose_metrics"
        )
    }
}
