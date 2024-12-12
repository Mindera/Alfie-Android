package au.com.alfie.ecomm.buildconvention.plugin

import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.DESTINATIONS_CORE
import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.DESTINATIONS_KSP
import au.com.alfie.ecomm.buildconvention.dependency.KotlinDependency.KSP
import au.com.alfie.ecomm.buildconvention.extension.implementation
import au.com.alfie.ecomm.buildconvention.extension.lib
import au.com.alfie.ecomm.buildconvention.extension.libs
import au.com.alfie.ecomm.buildconvention.extension.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class DestinationsConventionPlugin : Plugin<Project> {

    companion object {
        const val ID = "alfie.destinations"
    }

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply(libs.plugin(KSP))
            }

            dependencies {
                implementation(libs.lib(DESTINATIONS_CORE))
                add("ksp", libs.lib(DESTINATIONS_KSP))
            }
        }
    }
}
