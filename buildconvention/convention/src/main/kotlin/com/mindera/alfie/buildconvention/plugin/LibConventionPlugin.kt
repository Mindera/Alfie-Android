package com.mindera.alfie.buildconvention.plugin

import com.mindera.alfie.buildconvention.dependency.AndroidDependency.ANDROID_LIBRARY
import com.mindera.alfie.buildconvention.dependency.KotlinDependency.COLLECTIONS_IMMUTABLE
import com.mindera.alfie.buildconvention.dependency.KotlinDependency.COROUTINES
import com.mindera.alfie.buildconvention.dependency.KotlinDependency.KOTLIN_ANDROID
import com.mindera.alfie.buildconvention.dependency.KotlinDependency.KOVER
import com.mindera.alfie.buildconvention.dependency.TestDependency.ANDROID_JUNIT5
import com.mindera.alfie.buildconvention.dependency.ThirdPartyDependency.DETEKT
import com.mindera.alfie.buildconvention.dependency.ThirdPartyDependency.TIMBER
import com.mindera.alfie.buildconvention.extension.implementation
import com.mindera.alfie.buildconvention.extension.lib
import com.mindera.alfie.buildconvention.extension.libs
import com.mindera.alfie.buildconvention.extension.plugin
import com.mindera.alfie.buildconvention.plugin.configuration.configureDetekt
import com.mindera.alfie.buildconvention.plugin.configuration.configureKotlinAndroid
import com.mindera.alfie.buildconvention.plugin.configuration.configureUnitTest
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
