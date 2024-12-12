package au.com.alfie.ecomm.buildconvention.extension

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.version(alias: String): String = findVersion(alias).get().toString()

internal fun VersionCatalog.plugin(alias: String): String = findPlugin(alias).get().get().pluginId

internal fun VersionCatalog.lib(alias: String) = findLibrary(alias).get()
