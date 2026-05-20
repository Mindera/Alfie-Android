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
import com.mindera.alfie.buildconvention.dependency.KotlinDependency.KOTLIN_COMPILER_EXTENSION
import com.mindera.alfie.buildconvention.extension.implementation
import com.mindera.alfie.buildconvention.extension.lib
import com.mindera.alfie.buildconvention.extension.libs
import com.mindera.alfie.buildconvention.extension.version
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            // https://developer.android.com/jetpack/androidx/releases/compose-kotlin#pre-release_kotlin_compatibility
            kotlinCompilerExtensionVersion = libs.version(KOTLIN_COMPILER_EXTENSION)
        }

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
}
