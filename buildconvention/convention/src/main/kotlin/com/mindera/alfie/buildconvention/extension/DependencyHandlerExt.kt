package com.mindera.alfie.buildconvention.extension

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.project

fun DependencyHandler.api(vararg dependencies: String) {
    dependencies.forEach { dependency ->
        add("api", dependency)
    }
}

fun DependencyHandler.implementation(vararg dependencies: Provider<MinimalExternalModuleDependency>) {
    dependencies.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.implementation(vararg dependencies: String) {
    dependencies.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.projectImplementation(dependencies: List<String>) {
    dependencies.forEach { dependency ->
        add("implementation", project(dependency))
    }
}

fun DependencyHandler.debugImplementation(vararg dependencies: String) {
    dependencies.forEach { dependency ->
        add("debugImplementation", dependency)
    }
}

fun DependencyHandler.ksp(vararg dependencies: Provider<MinimalExternalModuleDependency>) {
    dependencies.forEach { dependency ->
        add("ksp", dependency)
    }
}

fun DependencyHandler.ksp(vararg dependencies: String) {
    dependencies.forEach { dependency ->
        add("ksp", dependency)
    }
}

fun DependencyHandler.compileOnly(vararg dependencies: String) {
    dependencies.forEach { dependency ->
        add("compileOnly", dependency)
    }
}

fun DependencyHandler.testImplementation(vararg dependencies: String) {
    dependencies.forEach { dependency ->
        add("testImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(vararg dependencies: Provider<MinimalExternalModuleDependency>) {
    dependencies.forEach { dependency ->
        add("testImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(vararg dependencies: Dependency) {
    dependencies.forEach { dependency ->
        add("testImplementation", dependency)
    }
}

fun DependencyHandler.testRuntimeOnly(vararg dependencies: Provider<MinimalExternalModuleDependency>) {
    dependencies.forEach { dependency ->
        add("testRuntimeOnly", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(vararg dependencies: String) {
    dependencies.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(vararg dependencies: Provider<MinimalExternalModuleDependency>) {
    dependencies.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(vararg dependencies: Dependency) {
    dependencies.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.androidTestRuntimeOnly(vararg dependencies: Provider<MinimalExternalModuleDependency>) {
    dependencies.forEach { dependency ->
        add("androidTestRuntimeOnly", dependency)
    }
}

fun DependencyHandler.kover(dependencies: List<String>) {
    dependencies.forEach { dependency ->
        add("kover", project(dependency))
    }
}

fun DependencyHandler.betaImplementation(dependency: ProjectDependency) {
    add("betaImplementation", dependency)
}

fun DependencyHandler.betaImplementation(dependency: Provider<MinimalExternalModuleDependency>) {
    add("betaImplementation", dependency)
}
