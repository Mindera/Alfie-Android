package au.com.alfie.ecomm.navigation

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.feature.account.destinations.AccountScreenDestination
import au.com.alfie.ecomm.feature.bag.destinations.BagScreenDestination
import au.com.alfie.ecomm.feature.debug.destinations.DebugScreenDestination
import au.com.alfie.ecomm.feature.home.destinations.HomeScreenDestination
import au.com.alfie.ecomm.feature.pdp.destinations.ProductDetailsScreenDestination
import au.com.alfie.ecomm.feature.plp.destinations.ProductListScreenDestination
import au.com.alfie.ecomm.feature.shop.destinations.ShopCategoryScreenDestination
import au.com.alfie.ecomm.feature.shop.destinations.ShopScreenDestination
import au.com.alfie.ecomm.feature.webview.destinations.WebViewScreenDestination
import au.com.alfie.ecomm.feature.wishlist.destinations.WishlistScreenDestination
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
