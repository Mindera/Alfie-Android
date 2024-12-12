package au.com.alfie.ecomm.buildconvention.plugin

import au.com.alfie.ecomm.buildconvention.dependency.FirebaseDependency.FIREBASE_ANALYTICS
import au.com.alfie.ecomm.buildconvention.dependency.FirebaseDependency.FIREBASE_BOM
import au.com.alfie.ecomm.buildconvention.dependency.FirebaseDependency.FIREBASE_COMMON_KTX
import au.com.alfie.ecomm.buildconvention.dependency.FirebaseDependency.FIREBASE_CRASHLYTICS
import au.com.alfie.ecomm.buildconvention.dependency.FirebaseDependency.GOOGLE_SERVICES
import au.com.alfie.ecomm.buildconvention.extension.implementation
import au.com.alfie.ecomm.buildconvention.extension.lib
import au.com.alfie.ecomm.buildconvention.extension.libs
import au.com.alfie.ecomm.buildconvention.extension.plugin
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
