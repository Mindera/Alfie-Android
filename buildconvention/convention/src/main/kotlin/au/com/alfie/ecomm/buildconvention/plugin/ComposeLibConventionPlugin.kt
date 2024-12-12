package au.com.alfie.ecomm.buildconvention.plugin

import au.com.alfie.ecomm.buildconvention.plugin.configuration.configureCompose
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

internal class ComposeLibConventionPlugin : Plugin<Project> {

    companion object {
        const val ID = "alfie.compose.lib"
    }

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply(LibConventionPlugin.ID)
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureCompose(extension)
        }
    }
}
