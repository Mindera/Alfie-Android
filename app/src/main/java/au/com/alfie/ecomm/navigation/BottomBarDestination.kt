package au.com.alfie.ecomm.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.R
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.navigation.arguments.shop.shopNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.wishlist.wishlistNavArgs
import au.com.alfie.ecomm.core.ui.test.BAG_TAB
import au.com.alfie.ecomm.core.ui.test.HOME_TAB
import au.com.alfie.ecomm.core.ui.test.SHOP_TAB
import au.com.alfie.ecomm.core.ui.test.WISHLIST_TAB
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarItem
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarItemState
import au.com.alfie.ecomm.designsystem.component.bottombar.rememberBottomBarItemState
import au.com.alfie.ecomm.feature.bag.destinations.BagScreenDestination
import au.com.alfie.ecomm.feature.home.destinations.HomeScreenDestination
import au.com.alfie.ecomm.feature.shop.destinations.ShopCategoryScreenDestination
import au.com.alfie.ecomm.feature.shop.destinations.ShopScreenDestination
import au.com.alfie.ecomm.feature.wishlist.destinations.WishlistScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.Direction
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import au.com.alfie.ecomm.designsystem.R as RD

@Stable
data class BottomBarDestination(
    val direction: Direction,
    override val state: BottomBarItemState,
    @DrawableRes override val icon: Int,
    override val label: StringResource,
    override val testTag: String,
    val shouldSelect: (DestinationSpec<*>?) -> Boolean,
    val shouldRestore: (DestinationSpec<*>?) -> Boolean
) : BottomBarItem {

    fun updateSelectedState(currentDestination: DestinationSpec<*>?) {
        state.updateSelectedState(shouldSelect(currentDestination))
    }
}

fun ImmutableList<BottomBarDestination>.updateSelectedState(
    currentDestination: DestinationSpec<*>?
) = forEach {
    it.updateSelectedState(currentDestination)
}

@Composable
fun bottomBarItems(wishlistToggleEnabled: Boolean): PersistentList<BottomBarDestination> {
    val destinations = mutableListOf(
        BottomBarDestination(
            direction = HomeScreenDestination(),
            state = rememberBottomBarItemState(),
            icon = RD.drawable.ic_action_house,
            label = StringResource.fromId(id = R.string.bottom_bar_home),
            testTag = HOME_TAB,
            shouldSelect = { HomeScreenDestination == it },
            shouldRestore = { HomeScreenDestination == it }
        ),
        BottomBarDestination(
            direction = ShopScreenDestination(shopNavArgs()),
            state = rememberBottomBarItemState(),
            icon = RD.drawable.ic_informational_store,
            label = StringResource.fromId(id = R.string.bottom_bar_shop),
            testTag = SHOP_TAB,
            shouldSelect = { ShopScreenDestination == it || ShopCategoryScreenDestination == it },
            shouldRestore = { ShopScreenDestination == it }
        ),
        BottomBarDestination(
            direction = BagScreenDestination(),
            state = rememberBottomBarItemState(),
            icon = RD.drawable.ic_action_bag,
            label = StringResource.fromId(id = R.string.bottom_bar_bag),
            testTag = BAG_TAB,
            shouldSelect = { BagScreenDestination == it },
            shouldRestore = { BagScreenDestination == it }
        )
    )

    if (wishlistToggleEnabled) {
        destinations.add(2, BottomBarDestination(
            direction = WishlistScreenDestination(wishlistNavArgs(launchFromTop = true)),
            state = rememberBottomBarItemState(),
            icon = RD.drawable.ic_action_heart_outline,
            label = StringResource.fromId(id = R.string.bottom_bar_wishlist),
            testTag = WISHLIST_TAB,
            shouldSelect = { WishlistScreenDestination == it },
            shouldRestore = { WishlistScreenDestination == it }
        ))
    }

    return persistentListOf<BottomBarDestination>().addAll(destinations)
}
