package au.com.alfie.ecomm.navigation

import androidx.compose.runtime.Immutable
import au.com.alfie.ecomm.core.navigation.NestedNavGraph
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
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import javax.inject.Inject

@Immutable
class NavGraphs @Inject constructor(
    private val nestedNavGraphs: Set<@JvmSuppressWildcards NestedNavGraph>
) {

    val root = object : NavGraphSpec {
        override val route = "root"

        override val startRoute = HomeScreenDestination

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            AccountScreenDestination,
            BagScreenDestination,
            ShopCategoryScreenDestination,
            DebugScreenDestination,
            HomeScreenDestination,
            ProductDetailsScreenDestination,
            ProductListScreenDestination,
            ShopScreenDestination,
            WebViewScreenDestination,
            WishlistScreenDestination
        ).associateBy { it.route }

        override val nestedNavGraphs: List<NavGraphSpec> = this@NavGraphs.nestedNavGraphs.map { it.navGraphSpec }
    }
}
