package com.mindera.alfie.buildconvention.plugin.configuration

import com.mindera.alfie.buildconvention.AppConfig
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion.VERSION_17
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

// AGP 9 made CommonExtension non-generic and exposes its blocks as plain properties;
// the block form (defaultConfig { }, compileOptions { }) now only exists on the
// concrete ApplicationExtension/LibraryExtension types.
internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension) {
    commonExtension.apply {
        compileSdk = AppConfig.compileSdk

        defaultConfig.minSdk = AppConfig.minSdk

        compileOptions.apply {
            sourceCompatibility = VERSION_17
            targetCompatibility = VERSION_17
        }
    }

    // android.kotlinOptions is deprecated: configure the Kotlin plugin directly instead.
    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}
