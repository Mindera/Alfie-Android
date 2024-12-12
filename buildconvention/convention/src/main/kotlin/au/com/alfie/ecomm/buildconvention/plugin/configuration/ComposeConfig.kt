package au.com.alfie.ecomm.buildconvention.plugin.configuration

import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.COMPOSE
import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.COMPOSE_ANIMATION
import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.COMPOSE_RUNTIME
import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.COMPOSE_TOOLING
import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.COMPOSE_TOOLING_PREVIEW
import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.COMPOSE_TRACING
import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.COMPOSE_UI
import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.MATERIAL
import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.MATERIAL3
import au.com.alfie.ecomm.buildconvention.dependency.KotlinDependency.KOTLIN_COMPILER_EXTENSION
import au.com.alfie.ecomm.buildconvention.extension.implementation
import au.com.alfie.ecomm.buildconvention.extension.lib
import au.com.alfie.ecomm.buildconvention.extension.libs
import au.com.alfie.ecomm.buildconvention.extension.version
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
