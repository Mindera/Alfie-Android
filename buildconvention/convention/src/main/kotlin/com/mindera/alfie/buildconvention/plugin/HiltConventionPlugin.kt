package com.mindera.alfie.buildconvention.plugin

import com.mindera.alfie.buildconvention.dependency.HiltDependency.HILT
import com.mindera.alfie.buildconvention.dependency.HiltDependency.HILT_COMPILER
import com.mindera.alfie.buildconvention.dependency.KotlinDependency.KSP
import com.mindera.alfie.buildconvention.extension.implementation
import com.mindera.alfie.buildconvention.extension.ksp
import com.mindera.alfie.buildconvention.extension.lib
import com.mindera.alfie.buildconvention.extension.libs
import com.mindera.alfie.buildconvention.extension.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class HiltConventionPlugin : Plugin<Project> {

    companion object {
        const val ID = "alfie.hilt"
    }

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugin(HILT))

                apply(libs.plugin(KSP))
            }

            dependencies {
                implementation(libs.lib(HILT))
                ksp(libs.lib(HILT_COMPILER))
            }
        }
    }
}
