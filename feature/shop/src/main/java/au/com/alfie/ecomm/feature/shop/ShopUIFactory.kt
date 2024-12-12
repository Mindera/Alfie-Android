package au.com.alfie.ecomm.feature.shop

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.feature.shop.model.ShopUI
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ShopUIFactory @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val environmentManager: EnvironmentManager
) {
    companion object {
        internal const val SERVICES_WEB_URL = "services/store-services"

        val PLACEHOLDER = ShopUI(
            servicesUrl = ""
        )
    }

    suspend operator fun invoke(): ShopUI = withContext(dispatcher.default()) {
        val environment = environmentManager.current()
        ShopUI(
            servicesUrl = "${environment.webUrl}/$SERVICES_WEB_URL"
        )
    }
}
