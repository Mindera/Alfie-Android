package au.com.alfie.ecomm.buildconvention.plugin

import au.com.alfie.ecomm.buildconvention.dependency.AndroidDependency.APPCOMPAT
import au.com.alfie.ecomm.buildconvention.dependency.AndroidDependency.LIFECYCLE_RUNTIME_COMPOSE
import au.com.alfie.ecomm.buildconvention.dependency.AndroidDependency.LIFECYCLE_VIEW_MODEL
import au.com.alfie.ecomm.buildconvention.dependency.AndroidDependency.LIFECYCLE_VIEW_MODEL_COMPOSE
import au.com.alfie.ecomm.buildconvention.dependency.HiltDependency.HILT_NAVIGATION
import au.com.alfie.ecomm.buildconvention.extension.implementation
import au.com.alfie.ecomm.buildconvention.extension.lib
import au.com.alfie.ecomm.buildconvention.extension.libs
import au.com.alfie.ecomm.buildconvention.extension.projectImplementation
import au.com.alfie.ecomm.buildconvention.module.ProjectModule
import au.com.alfie.ecomm.buildconvention.plugin.configuration.configureCompose
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
