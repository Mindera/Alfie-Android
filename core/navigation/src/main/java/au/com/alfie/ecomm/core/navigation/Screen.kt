package au.com.alfie.ecomm.core.navigation

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.core.navigation.arguments.CategoryNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.ProductDetailsNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.productlist.ProductListNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.shop.ShopNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.webview.WebViewNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.wishlist.WishlistNavArgs

@Stable
sealed interface Screen {

    data object Account : Screen

    data object Bag : Screen

    data class Category(val args: CategoryNavArgs) : Screen

    data object Debug : Screen

    data object Home : Screen

    data class ProductDetails(val args: ProductDetailsNavArgs) : Screen

    data class ProductList(val args: ProductListNavArgs) : Screen

    data class Shop(val args: ShopNavArgs) : Screen

    data class WebView(val args: WebViewNavArgs) : Screen

    data class Wishlist(val args: WishlistNavArgs) : Screen
}
