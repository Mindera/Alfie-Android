package com.mindera.alfie.buildconvention.plugin

import com.mindera.alfie.buildconvention.dependency.ComposeDependency.DESTINATIONS_CORE
import com.mindera.alfie.buildconvention.dependency.ComposeDependency.DESTINATIONS_KSP
import com.mindera.alfie.buildconvention.dependency.KotlinDependency.KSP
import com.mindera.alfie.buildconvention.extension.implementation
import com.mindera.alfie.buildconvention.extension.lib
import com.mindera.alfie.buildconvention.extension.libs
import com.mindera.alfie.buildconvention.extension.plugin
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
