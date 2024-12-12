package au.com.alfie.ecomm.buildconvention.plugin.configuration

import au.com.alfie.ecomm.buildconvention.AppConfig
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion.VERSION_17
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

internal fun configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = AppConfig.compileSdk

        defaultConfig {
            minSdk = AppConfig.minSdk
        }

        compileOptions {
            sourceCompatibility = VERSION_17
            targetCompatibility = VERSION_17
        }

        kotlinOptions {
            jvmTarget = VERSION_17.toString()
        }
    }
}

fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}
