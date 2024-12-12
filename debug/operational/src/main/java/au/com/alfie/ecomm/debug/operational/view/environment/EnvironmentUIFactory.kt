package au.com.alfie.ecomm.debug.operational.view.environment

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.debug.operational.R
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentUI
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class EnvironmentUIFactory @Inject constructor(
    private val environmentManager: EnvironmentManager,
    private val dispatcher: DispatcherProvider
) {

    suspend operator fun invoke() = withContext(dispatcher.default()) {
        val currentEnv = environmentManager.current()
        val environments = environmentManager.environments()
        val customEnv = environmentManager.custom()

        buildList {
            add(
                EnvironmentUI(
                    environment = environments.dev,
                    isEnabled = true,
                    isSelected = environments.dev == currentEnv,
                    enableUrlChange = false,
                    label = StringResource.fromId(R.string.environment_dev)
                )
            )
            add(
                EnvironmentUI(
                    environment = environments.preProd,
                    isEnabled = false,
                    isSelected = environments.preProd == currentEnv,
                    enableUrlChange = false,
                    label = StringResource.fromId(R.string.environment_pre_prod)
                )
            )
            add(
                EnvironmentUI(
                    environment = environments.prod,
                    isEnabled = false,
                    isSelected = environments.prod == currentEnv,
                    enableUrlChange = false,
                    label = StringResource.fromId(R.string.environment_prod)
                )
            )
            add(
                EnvironmentUI(
                    environment = customEnv,
                    isEnabled = true,
                    isSelected = customEnv == currentEnv,
                    enableUrlChange = true,
                    label = StringResource.fromId(R.string.environment_custom)
                )
            )
        }
    }
}
