package au.com.alfie.ecomm.buildconvention.plugin.configuration

import au.com.alfie.ecomm.buildconvention.AppConfig
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.COROUTINES_TEST
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.FIXTURE
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.JUNIT5
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.JUNIT5_ENGINE
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.JUNIT5_PARAMS
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.JUNIT5_RUNNER
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.KOTLIN_JUNIT5
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.MOCKK
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.MOCKK_ANDROID
import au.com.alfie.ecomm.buildconvention.dependency.TestDependency.TURBINE
import au.com.alfie.ecomm.buildconvention.extension.androidTestImplementation
import au.com.alfie.ecomm.buildconvention.extension.androidTestRuntimeOnly
import au.com.alfie.ecomm.buildconvention.extension.lib
import au.com.alfie.ecomm.buildconvention.extension.libs
import au.com.alfie.ecomm.buildconvention.extension.testImplementation
import au.com.alfie.ecomm.buildconvention.extension.testRuntimeOnly
import au.com.alfie.ecomm.buildconvention.module.ProjectModule
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
