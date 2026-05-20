package com.mindera.alfie.navigation

import androidx.compose.runtime.Immutable
import com.mindera.alfie.core.navigation.NestedNavGraph
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
