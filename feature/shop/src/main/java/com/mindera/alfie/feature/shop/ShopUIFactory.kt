package com.mindera.alfie.feature.shop

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.environment.EnvironmentManager
import com.mindera.alfie.feature.shop.model.ShopUI
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
