package au.com.alfie.ecomm.buildconvention.plugin

import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.dependency.AndroidDependency.ANDROID_APPLICATION
import au.com.alfie.ecomm.buildconvention.dependency.AndroidDependency.APPCOMPAT
import au.com.alfie.ecomm.buildconvention.dependency.AndroidDependency.CORE_KTX
import au.com.alfie.ecomm.buildconvention.dependency.AndroidDependency.LIFECYCLE_RUNTIME_COMPOSE
import au.com.alfie.ecomm.buildconvention.dependency.AndroidDependency.LIFECYCLE_VIEW_MODEL
import au.com.alfie.ecomm.buildconvention.dependency.AndroidDependency.LIFECYCLE_VIEW_MODEL_COMPOSE
import au.com.alfie.ecomm.buildconvention.dependency.ComposeDependency.COMPOSE_ACTIVITY
import au.com.alfie.ecomm.buildconvention.dependency.KotlinDependency.KOTLIN_ANDROID
import au.com.alfie.ecomm.buildconvention.dependency.KotlinDependency.KOVER
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.ANDROID_JUNIT5
import au.com.alfie.ecomm.buildconvention.dependency.ThirdPartyDependency.DETEKT
import au.com.alfie.ecomm.buildconvention.dependency.ThirdPartyDependency.DETEKT_COMPOSE
import au.com.alfie.ecomm.buildconvention.dependency.ThirdPartyDependency.DETEKT_FORMATTER
import au.com.alfie.ecomm.buildconvention.dependency.ThirdPartyDependency.TIMBER
import au.com.alfie.ecomm.buildconvention.extension.implementation
import au.com.alfie.ecomm.buildconvention.extension.kover
import au.com.alfie.ecomm.buildconvention.extension.lib
import au.com.alfie.ecomm.buildconvention.extension.libs
import au.com.alfie.ecomm.buildconvention.extension.plugin
import au.com.alfie.ecomm.buildconvention.extension.projectImplementation
import au.com.alfie.ecomm.buildconvention.module.FeatureModule
import au.com.alfie.ecomm.buildconvention.module.ProjectModule
import au.com.alfie.ecomm.buildconvention.plugin.configuration.configureCompose
import au.com.alfie.ecomm.buildconvention.plugin.configuration.configureKotlinAndroid
import au.com.alfie.ecomm.buildconvention.plugin.configuration.configureUnitTest
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
                            "au.com.alfie.ecomm.navigation"
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
