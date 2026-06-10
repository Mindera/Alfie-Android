package com.mindera.alfie.buildconvention.plugin.configuration

import com.mindera.alfie.buildconvention.AppConfig
import com.mindera.alfie.buildconvention.dependency.TestDependency.COROUTINES_TEST
import com.mindera.alfie.buildconvention.dependency.TestDependency.FIXTURE
import com.mindera.alfie.buildconvention.dependency.TestDependency.JUNIT5
import com.mindera.alfie.buildconvention.dependency.TestDependency.JUNIT5_ENGINE
import com.mindera.alfie.buildconvention.dependency.TestDependency.JUNIT4
import com.mindera.alfie.buildconvention.dependency.TestDependency.JUNIT5_PARAMS
import com.mindera.alfie.buildconvention.dependency.TestDependency.JUNIT5_RUNNER
import com.mindera.alfie.buildconvention.dependency.TestDependency.JUNIT5_VINTAGE
import com.mindera.alfie.buildconvention.dependency.TestDependency.KOTLIN_JUNIT5
import com.mindera.alfie.buildconvention.dependency.TestDependency.MOCKK
import com.mindera.alfie.buildconvention.dependency.TestDependency.MOCKK_ANDROID
import com.mindera.alfie.buildconvention.dependency.TestDependency.ROBOLECTRIC
import com.mindera.alfie.buildconvention.dependency.TestDependency.TURBINE
import com.mindera.alfie.buildconvention.extension.androidTestImplementation
import com.mindera.alfie.buildconvention.extension.androidTestRuntimeOnly
import com.mindera.alfie.buildconvention.extension.lib
import com.mindera.alfie.buildconvention.extension.libs
import com.mindera.alfie.buildconvention.extension.testImplementation
import com.mindera.alfie.buildconvention.extension.testRuntimeOnly
import com.mindera.alfie.buildconvention.module.ProjectModule
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureUnitTest(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        defaultConfig {
            testInstrumentationRunner = AppConfig.testRunnerPath
            testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"
        }

        dependencies {
            testImplementation(libs.lib(JUNIT5))
            testRuntimeOnly(libs.lib(JUNIT5_ENGINE))
            testImplementation(libs.lib(KOTLIN_JUNIT5))
            testImplementation(libs.lib(MOCKK))
            testImplementation(libs.lib(COROUTINES_TEST))
            testImplementation(libs.lib(TURBINE))
            testImplementation(libs.lib(FIXTURE))
            testImplementation(project(ProjectModule.coreTest))
            // Robolectric ships only a JUnit4 runner; junit-vintage-engine lets the JUnit5 Platform
            // discover and run those JUnit4 (@RunWith) tests. Without vintage they are silently skipped.
            testImplementation(libs.lib(ROBOLECTRIC))
            testImplementation(libs.lib(JUNIT4))
            testRuntimeOnly(libs.lib(JUNIT5_VINTAGE))
            androidTestImplementation(libs.lib(JUNIT5))
            androidTestRuntimeOnly(libs.lib(JUNIT5_RUNNER))
            androidTestImplementation(libs.lib(JUNIT5_PARAMS))
            androidTestImplementation(libs.lib(KOTLIN_JUNIT5))
            androidTestImplementation(libs.lib(MOCKK))
            androidTestImplementation(libs.lib(MOCKK_ANDROID))
            androidTestImplementation(libs.lib(COROUTINES_TEST))
            androidTestImplementation(libs.lib(TURBINE))
            androidTestImplementation(project(ProjectModule.coreTest))
        }
    }
}
