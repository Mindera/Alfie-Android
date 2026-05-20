package com.mindera.alfie.buildconvention.plugin

import com.mindera.alfie.buildconvention.dependency.HiltDependency.HILT
import com.mindera.alfie.buildconvention.dependency.HiltDependency.HILT_COMPILER
import com.mindera.alfie.buildconvention.dependency.KotlinDependency.KAPT
import com.mindera.alfie.buildconvention.extension.implementation
import com.mindera.alfie.buildconvention.extension.kapt
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

                // keep in the last line
                apply(libs.plugin(KAPT))
            }

            dependencies {
                implementation(libs.lib(HILT))
                kapt(libs.lib(HILT_COMPILER))
            }
        }
    }
}
