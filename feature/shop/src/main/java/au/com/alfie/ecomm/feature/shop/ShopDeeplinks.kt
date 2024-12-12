package au.com.alfie.ecomm.feature.shop

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.core.deeplink.DeeplinkInterpreter
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkInstance
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkSpec
import au.com.alfie.ecomm.core.deeplink.model.deeplinkSpec
import au.com.alfie.ecomm.core.navigation.arguments.shop.ShopTab
import au.com.alfie.ecomm.core.navigation.arguments.shop.shopNavArgs
import au.com.alfie.ecomm.feature.shop.destinations.ShopScreenDestination
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShopDeeplinks @Inject constructor() : DeeplinkGroup {

    companion object {
        private const val SHOP_PATH_SEGMENT = "shop"
        private const val BRAND_FIXED_SEGMENT = "brand"
        private const val BRANDS_FIXED_SEGMENT = "brands"
        private const val SERVICES_FIXED_SEGMENT = "services"
        private const val STORE_SERVICES_FIXED_SEGMENT = "store-services"
    }

    override val interpreters: List<DeeplinkInterpreter> = listOf(
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/shop
                deeplinkSpec {
                    appendFixedPathSegment(SHOP_PATH_SEGMENT)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = DeeplinkResult.NavigateClearingStack(
                direction = ShopScreenDestination(shopNavArgs()),
                saveState = true,
                restoreState = true,
                launchSingleTop = true
            )
        },
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/brand
                deeplinkSpec {
                    appendFixedPathSegment(BRAND_FIXED_SEGMENT)
                },
                // https://www.alfie.com/brands
                deeplinkSpec {
                    appendFixedPathSegment(BRANDS_FIXED_SEGMENT)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = DeeplinkResult.NavigateClearingStack(
                direction = ShopScreenDestination(shopNavArgs(ShopTab.Brands)),
                saveState = true,
                restoreState = false,
                launchSingleTop = true
            )
        },
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/services
                deeplinkSpec {
                    appendFixedPathSegment(SERVICES_FIXED_SEGMENT)
                },
                // https://www.alfie.com/services/store-services
                deeplinkSpec {
                    appendFixedPathSegment(SERVICES_FIXED_SEGMENT)
                    appendFixedPathSegment(STORE_SERVICES_FIXED_SEGMENT)
                },
                // https://www.alfie.com/store-services
                deeplinkSpec {
                    appendFixedPathSegment(STORE_SERVICES_FIXED_SEGMENT)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = DeeplinkResult.NavigateClearingStack(
                direction = ShopScreenDestination(shopNavArgs(ShopTab.Services)),
                saveState = true,
                restoreState = false,
                launchSingleTop = true
            )
        }
    )
}
