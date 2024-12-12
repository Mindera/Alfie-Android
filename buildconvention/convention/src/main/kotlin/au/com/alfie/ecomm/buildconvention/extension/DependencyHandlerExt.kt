package au.com.alfie.ecomm.buildconvention.extension

import org.gradle.api.Project
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

fun DependencyHandler.kapt(vararg dependencies: Provider<MinimalExternalModuleDependency>) {
    dependencies.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.kapt(vararg dependencies: String) {
    dependencies.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.compileOnly(vararg dependencies: String) {
    dependencies.forEach { dependency ->
        add("compileOnly", dependency)
    }
}

fun DependencyHandler.kaptAndroidTest(vararg dependencies: String) {
    dependencies.forEach { dependency ->
        add("kaptAndroidTest", dependency)
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

fun DependencyHandler.testImplementation(vararg dependencies: Project) {
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

fun DependencyHandler.androidTestImplementation(vararg dependencies: Project) {
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
