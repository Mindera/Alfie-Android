package au.com.alfie.ecomm.buildconvention.plugin.configuration

import au.com.alfie.ecomm.buildconvention.dependency.ThirdPartyDependency
import au.com.alfie.ecomm.buildconvention.extension.lib
import au.com.alfie.ecomm.buildconvention.extension.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureDetekt(detektExtension: DetektExtension) {
    detektExtension.apply {
        buildUponDefaultConfig = true
        ignoreFailures = false
        parallel = true
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
        baseline = file("$rootDir/config/detekt/baseline.xml")
    }

    dependencies {
        add("detektPlugins", libs.lib(ThirdPartyDependency.DETEKT_FORMATTER))
        add("detektPlugins", libs.lib(ThirdPartyDependency.DETEKT_COMPOSE))
    }
}
