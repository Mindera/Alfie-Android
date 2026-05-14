package com.mindera.alfie.feature.wishlist

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.core.deeplink.DeeplinkInterpreter
import com.mindera.alfie.core.deeplink.DeeplinkResult
import com.mindera.alfie.core.deeplink.model.DeeplinkInstance
import com.mindera.alfie.core.deeplink.model.DeeplinkSpec
import com.mindera.alfie.core.deeplink.model.deeplinkSpec
import com.mindera.alfie.core.navigation.arguments.wishlist.wishlistNavArgs
import com.mindera.alfie.feature.wishlist.destinations.WishlistScreenDestination
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
