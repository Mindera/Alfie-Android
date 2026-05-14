package com.mindera.alfie.navigation

import androidx.compose.runtime.Stable
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.feature.account.destinations.AccountScreenDestination
import com.mindera.alfie.feature.bag.destinations.BagScreenDestination
import com.mindera.alfie.feature.debug.destinations.DebugScreenDestination
import com.mindera.alfie.feature.home.destinations.HomeScreenDestination
import com.mindera.alfie.feature.pdp.destinations.ProductDetailsScreenDestination
import com.mindera.alfie.feature.plp.destinations.ProductListScreenDestination
import com.mindera.alfie.feature.shop.destinations.ShopCategoryScreenDestination
import com.mindera.alfie.feature.shop.destinations.ShopScreenDestination
import com.mindera.alfie.feature.webview.destinations.WebViewScreenDestination
import com.mindera.alfie.feature.wishlist.destinations.WishlistScreenDestination
import com.ramcosta.composedestinations.spec.Direction
import javax.inject.Inject

@Stable
class DirectionProviderImpl @Inject constructor() : DirectionProvider {

    override fun fromScreen(screen: Screen): Direction = when (screen) {
        is Screen.Account -> AccountScreenDestination
        is Screen.Bag -> BagScreenDestination
        is Screen.Category -> ShopCategoryScreenDestination(screen.args)
        is Screen.Debug -> DebugScreenDestination
        is Screen.Home -> HomeScreenDestination
        is Screen.ProductDetails -> ProductDetailsScreenDestination(screen.args)
        is Screen.ProductList -> ProductListScreenDestination(screen.args)
        is Screen.Shop -> ShopScreenDestination(screen.args)
        is Screen.WebView -> WebViewScreenDestination(screen.args)
        is Screen.Wishlist -> WishlistScreenDestination(screen.args)
    }
}
