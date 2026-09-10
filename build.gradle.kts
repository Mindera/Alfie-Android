import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.junit5) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
}

dependencies {
    detektPlugins(libs.detekt.formatter)
    detektPlugins(libs.detekt.compose)
}

extra.set("versionNameConfig", "0.8.1")
extra.set("versionCodeConfig", 9)

tasks.register<DetektCreateBaselineTask>("detektAutoFix") {
    setup()
    autoCorrect = true
}

tasks.register<DetektCreateBaselineTask>("detektProjectBaseline") {
    setup()
}

fun DetektCreateBaselineTask.setup() {
    description = "Overrides current baseline."
    buildUponDefaultConfig.set(true)
    ignoreFailures.set(true)
    parallel.set(true)
    setSource(file(projectDir))
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    baseline.set(file("$rootDir/config/detekt/baseline.xml"))
    include("**/*.kt")
    exclude("**/resources/**")
    exclude("**/build/**")
}