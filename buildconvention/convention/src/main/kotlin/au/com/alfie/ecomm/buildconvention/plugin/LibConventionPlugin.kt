package au.com.alfie.ecomm.buildconvention.plugin

import au.com.alfie.ecomm.buildconvention.dependency.AndroidDependency.ANDROID_LIBRARY
import au.com.alfie.ecomm.buildconvention.dependency.KotlinDependency.COLLECTIONS_IMMUTABLE
import au.com.alfie.ecomm.buildconvention.dependency.KotlinDependency.COROUTINES
import au.com.alfie.ecomm.buildconvention.dependency.KotlinDependency.KOTLIN_ANDROID
import au.com.alfie.ecomm.buildconvention.dependency.KotlinDependency.KOVER
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.ANDROID_JUNIT5
import au.com.alfie.ecomm.buildconvention.dependency.ThirdPartyDependency.DETEKT
import au.com.alfie.ecomm.buildconvention.dependency.ThirdPartyDependency.TIMBER
import au.com.alfie.ecomm.buildconvention.extension.implementation
import au.com.alfie.ecomm.buildconvention.extension.lib
import au.com.alfie.ecomm.buildconvention.extension.libs
import au.com.alfie.ecomm.buildconvention.extension.plugin
import au.com.alfie.ecomm.buildconvention.plugin.configuration.configureDetekt
import au.com.alfie.ecomm.buildconvention.plugin.configuration.configureKotlinAndroid
import au.com.alfie.ecomm.buildconvention.plugin.configuration.configureUnitTest
import com.android.build.gradle.LibraryExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class LibConventionPlugin : Plugin<Project> {

    companion object {
        const val ID = "alfie.lib"
    }

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugin(ANDROID_LIBRARY))
                apply(libs.plugin(KOTLIN_ANDROID))
                apply(libs.plugin(DETEKT))
                apply(libs.plugin(ANDROID_JUNIT5))
                apply(libs.plugin(KOVER))
                apply(HiltConventionPlugin.ID)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureUnitTest(this)
            }

            extensions.configure<DetektExtension> {
                configureDetekt(this)
            }

            dependencies {
                implementation(libs.lib(COROUTINES))
                implementation(libs.lib(TIMBER))
                implementation(libs.lib(COLLECTIONS_IMMUTABLE))
            }
        }
    }
}
