package au.com.alfie.ecomm.buildconvention.plugin

import au.com.alfie.ecomm.buildconvention.dependency.HiltDependency.HILT
import au.com.alfie.ecomm.buildconvention.dependency.HiltDependency.HILT_COMPILER
import au.com.alfie.ecomm.buildconvention.dependency.KotlinDependency.KAPT
import au.com.alfie.ecomm.buildconvention.extension.implementation
import au.com.alfie.ecomm.buildconvention.extension.kapt
import au.com.alfie.ecomm.buildconvention.extension.lib
import au.com.alfie.ecomm.buildconvention.extension.libs
import au.com.alfie.ecomm.buildconvention.extension.plugin
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
