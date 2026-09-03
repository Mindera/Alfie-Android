package com.mindera.alfie.buildconvention.plugin.configuration

import com.mindera.alfie.buildconvention.dependency.ComposeDependency.COMPOSE
import com.mindera.alfie.buildconvention.dependency.ComposeDependency.COMPOSE_ANIMATION
import com.mindera.alfie.buildconvention.dependency.ComposeDependency.COMPOSE_RUNTIME
import com.mindera.alfie.buildconvention.dependency.ComposeDependency.COMPOSE_TOOLING
import com.mindera.alfie.buildconvention.dependency.ComposeDependency.COMPOSE_TOOLING_PREVIEW
import com.mindera.alfie.buildconvention.dependency.ComposeDependency.COMPOSE_TRACING
import com.mindera.alfie.buildconvention.dependency.ComposeDependency.COMPOSE_UI
import com.mindera.alfie.buildconvention.dependency.ComposeDependency.MATERIAL
import com.mindera.alfie.buildconvention.dependency.ComposeDependency.MATERIAL3
import com.mindera.alfie.buildconvention.dependency.KotlinDependency.COMPOSE_COMPILER
import com.mindera.alfie.buildconvention.extension.implementation
import com.mindera.alfie.buildconvention.extension.lib
import com.mindera.alfie.buildconvention.extension.libs
import com.mindera.alfie.buildconvention.extension.plugin
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureCompose(commonExtension: CommonExtension) {
    // Since Kotlin 2.0 the Compose compiler ships as a Kotlin compiler plugin instead of
    // AGP's composeOptions.kotlinCompilerExtensionVersion, and is versioned with Kotlin.
    pluginManager.apply(libs.plugin(COMPOSE_COMPILER))

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        reportsDestination.set(layout.buildDirectory.dir("compose_metrics"))
        metricsDestination.set(layout.buildDirectory.dir("compose_metrics"))
    }

    commonExtension.buildFeatures.compose = true

    dependencies {
        implementation(libs.lib(COMPOSE))
        implementation(libs.lib(COMPOSE_TOOLING))
        implementation(libs.lib(COMPOSE_TOOLING_PREVIEW))
        implementation(libs.lib(COMPOSE_UI))
        implementation(libs.lib(COMPOSE_RUNTIME))
        implementation(libs.lib(COMPOSE_TRACING))
        implementation(libs.lib(COMPOSE_ANIMATION))
        implementation(libs.lib(MATERIAL))
        implementation(libs.lib(MATERIAL3))
    }
}
