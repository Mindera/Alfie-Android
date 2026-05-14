package com.mindera.alfie.buildconvention.plugin

import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.dependency.AndroidDependency.ANDROID_APPLICATION
import com.mindera.alfie.buildconvention.dependency.AndroidDependency.APPCOMPAT
import com.mindera.alfie.buildconvention.dependency.AndroidDependency.CORE_KTX
import com.mindera.alfie.buildconvention.dependency.AndroidDependency.LIFECYCLE_RUNTIME_COMPOSE
import com.mindera.alfie.buildconvention.dependency.AndroidDependency.LIFECYCLE_VIEW_MODEL
import com.mindera.alfie.buildconvention.dependency.AndroidDependency.LIFECYCLE_VIEW_MODEL_COMPOSE
import com.mindera.alfie.buildconvention.dependency.ComposeDependency.COMPOSE_ACTIVITY
import com.mindera.alfie.buildconvention.dependency.KotlinDependency.KOTLIN_ANDROID
import com.mindera.alfie.buildconvention.dependency.KotlinDependency.KOVER
import com.mindera.alfie.buildconvention.dependency.TestDependency.ANDROID_JUNIT5
import com.mindera.alfie.buildconvention.dependency.ThirdPartyDependency.DETEKT
import com.mindera.alfie.buildconvention.dependency.ThirdPartyDependency.DETEKT_COMPOSE
import com.mindera.alfie.buildconvention.dependency.ThirdPartyDependency.DETEKT_FORMATTER
import com.mindera.alfie.buildconvention.dependency.ThirdPartyDependency.TIMBER
import com.mindera.alfie.buildconvention.extension.implementation
import com.mindera.alfie.buildconvention.extension.kover
import com.mindera.alfie.buildconvention.extension.lib
import com.mindera.alfie.buildconvention.extension.libs
import com.mindera.alfie.buildconvention.extension.plugin
import com.mindera.alfie.buildconvention.extension.projectImplementation
import com.mindera.alfie.buildconvention.module.FeatureModule
import com.mindera.alfie.buildconvention.module.ProjectModule
import com.mindera.alfie.buildconvention.plugin.configuration.configureCompose
import com.mindera.alfie.buildconvention.plugin.configuration.configureKotlinAndroid
import com.mindera.alfie.buildconvention.plugin.configuration.configureUnitTest
import com.android.build.api.dsl.ApplicationExtension
import kotlinx.kover.gradle.plugin.dsl.KoverReportExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class AppConventionPlugin : Plugin<Project> {

    companion object {
        const val ID = "alfie.application"
    }

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugin(ANDROID_APPLICATION))
                apply(libs.plugin(ANDROID_JUNIT5))
                apply(libs.plugin(DETEKT))
                apply(libs.plugin(KOTLIN_ANDROID))
                apply(libs.plugin(KOVER))
                apply(HiltConventionPlugin.ID)
            }

            extensions.configure<ApplicationExtension> {
                namespace = AppConfig.applicationId
                configureKotlinAndroid(this)
                configureCompose(this)
                configureUnitTest(this)

                defaultConfig {
                    applicationId = AppConfig.applicationId
                    targetSdk = AppConfig.targetSdk

                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }
            }

            extensions.configure<KoverReportExtension> {
                filters {
                    excludes {
                        annotatedBy(
                            "androidx.compose.runtime.Composable", // Compose
                            "dagger.Module", // Hilt
                            "*Generated*", // Hilt
                            "androidx.room.Dao" // Room
                        )
                        packages(
                            "*.destinations", // Compose Destinations
                            "*.navtype", // Compose Destinations
                            "*.graphql*", // GraphQL
                            "*.designsystem*",
                            "*.core.ui*",
                            "*.core.sync*",
                            "*.component",
                            "*.debug*",
                            "*.log",
                            "*.dispatcher",
                            "com.mindera.alfie.navigation"
                        )
                        classes(
                            "*ComposableSingletons*", // Compose
                            "*_*", // Hilt
                            "*.DestinationsKt", // Compose Destinations
                            "*NavArgsGetters*", // Compose Destinations
                            "*Application*",
                            "*Activity*",
                            "*Intent*",
                            "*.BuildConfig",
                            "*Deeplinks*", // Tested on androidTest
                            "*Database", // Room
                            "*Dao", // Room
                            "*Dao$*", // Room
                            "*Proto", // Proto DataStore
                            "*Proto$*", // Proto DataStore
                            "*ProtoKt", // Proto DataStore
                            "*ProtoKt$*", // Proto DataStore
                            "*ProtoKtKt" // Proto DataStore
                        )
                    }
                }
            }

            dependencies {
                projectImplementation(ProjectModule.modules + FeatureModule.modules)
                kover(ProjectModule.modules + FeatureModule.modules)

                implementation(libs.lib(APPCOMPAT))
                implementation(libs.lib(COMPOSE_ACTIVITY))
                implementation(libs.lib(CORE_KTX))
                implementation(libs.lib(LIFECYCLE_RUNTIME_COMPOSE))
                implementation(libs.lib(LIFECYCLE_VIEW_MODEL))
                implementation(libs.lib(LIFECYCLE_VIEW_MODEL_COMPOSE))
                implementation(libs.lib(TIMBER))

                add("detektPlugins", libs.lib(DETEKT_FORMATTER))
                add("detektPlugins", libs.lib(DETEKT_COMPOSE))
            }
        }
    }
}
