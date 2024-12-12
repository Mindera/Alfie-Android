import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.extension.implementation
import au.com.alfie.ecomm.buildconvention.module.ProjectModule
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(buildConvention.plugins.compose)
}

@Suppress("UnstableApiUsage")
android {
    namespace = AppConfig.applicationId + ".designsystem"

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(project(ProjectModule.coreCommons))
    implementation(project(ProjectModule.coreUi))
    implementation(libs.compose.activity)
    implementation(libs.glide)
    implementation(libs.zoomable)
    implementation(libs.window)
}

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
