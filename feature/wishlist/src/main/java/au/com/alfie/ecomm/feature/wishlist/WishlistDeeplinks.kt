package au.com.alfie.ecomm.feature.wishlist

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.core.deeplink.DeeplinkInterpreter
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkInstance
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkSpec
import au.com.alfie.ecomm.core.deeplink.model.deeplinkSpec
import au.com.alfie.ecomm.core.navigation.arguments.wishlist.wishlistNavArgs
import au.com.alfie.ecomm.feature.wishlist.destinations.WishlistScreenDestination
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class WishlistDeeplinks @Inject constructor() : DeeplinkGroup {

    companion object {
        private const val WISHLIST_PATH_SEGMENT = "wishlist"
    }

    override val interpreters: List<DeeplinkInterpreter> = listOf(
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/wishlist
                deeplinkSpec {
                    appendFixedPathSegment(WISHLIST_PATH_SEGMENT)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult =
                DeeplinkResult.NavigateTo(direction = WishlistScreenDestination(wishlistNavArgs()))
        }
    )
}
