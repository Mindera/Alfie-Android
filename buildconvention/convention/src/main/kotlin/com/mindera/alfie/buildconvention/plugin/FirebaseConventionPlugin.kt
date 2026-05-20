package com.mindera.alfie.buildconvention.plugin

import com.mindera.alfie.buildconvention.dependency.FirebaseDependency.FIREBASE_ANALYTICS
import com.mindera.alfie.buildconvention.dependency.FirebaseDependency.FIREBASE_BOM
import com.mindera.alfie.buildconvention.dependency.FirebaseDependency.FIREBASE_COMMON_KTX
import com.mindera.alfie.buildconvention.dependency.FirebaseDependency.FIREBASE_CRASHLYTICS
import com.mindera.alfie.buildconvention.dependency.FirebaseDependency.GOOGLE_SERVICES
import com.mindera.alfie.buildconvention.extension.implementation
import com.mindera.alfie.buildconvention.extension.lib
import com.mindera.alfie.buildconvention.extension.libs
import com.mindera.alfie.buildconvention.extension.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FirebaseConventionPlugin : Plugin<Project> {

    companion object {
        const val ID = "alfie.firebase"
    }

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugin(GOOGLE_SERVICES))
                apply(libs.plugin(FIREBASE_CRASHLYTICS))
            }

            dependencies {
                implementation(platform(libs.lib(FIREBASE_BOM)))
                implementation(libs.lib(FIREBASE_ANALYTICS))
                implementation(libs.lib(FIREBASE_COMMON_KTX))
                implementation(libs.lib(FIREBASE_CRASHLYTICS))
            }
        }
    }
}
