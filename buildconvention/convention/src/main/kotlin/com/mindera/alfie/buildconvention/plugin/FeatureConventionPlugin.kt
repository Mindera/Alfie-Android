package com.mindera.alfie.buildconvention.plugin

import com.mindera.alfie.buildconvention.dependency.AndroidDependency.APPCOMPAT
import com.mindera.alfie.buildconvention.dependency.AndroidDependency.LIFECYCLE_RUNTIME_COMPOSE
import com.mindera.alfie.buildconvention.dependency.AndroidDependency.LIFECYCLE_VIEW_MODEL
import com.mindera.alfie.buildconvention.dependency.AndroidDependency.LIFECYCLE_VIEW_MODEL_COMPOSE
import com.mindera.alfie.buildconvention.dependency.HiltDependency.HILT_NAVIGATION
import com.mindera.alfie.buildconvention.extension.implementation
import com.mindera.alfie.buildconvention.extension.lib
import com.mindera.alfie.buildconvention.extension.libs
import com.mindera.alfie.buildconvention.extension.projectImplementation
import com.mindera.alfie.buildconvention.module.ProjectModule
import com.mindera.alfie.buildconvention.plugin.configuration.configureCompose
import com.android.build.gradle.LibraryExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal class FeatureConventionPlugin : Plugin<Project> {

    companion object {
        const val ID = "alfie.feature"
    }

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply(LibConventionPlugin.ID)
                apply(DestinationsConventionPlugin.ID)
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureCompose(extension)

            extensions.configure<KspExtension> {
                arg("compose-destinations.mode", "destinations")
            }

            val projectDependencies = listOf(
                ProjectModule.coreAnalytics,
                ProjectModule.coreCommons,
                ProjectModule.coreDeeplink,
                ProjectModule.coreNavigation,
                ProjectModule.coreUi,
                ProjectModule.designSystem,
                ProjectModule.domain,
                ProjectModule.feature
            )

            dependencies {
                projectImplementation(projectDependencies)

                implementation(libs.lib(APPCOMPAT))
                implementation(libs.lib(LIFECYCLE_VIEW_MODEL))
                implementation(libs.lib(LIFECYCLE_RUNTIME_COMPOSE))
                implementation(libs.lib(LIFECYCLE_VIEW_MODEL_COMPOSE))
                implementation(libs.lib(HILT_NAVIGATION))
            }
        }
    }
}
