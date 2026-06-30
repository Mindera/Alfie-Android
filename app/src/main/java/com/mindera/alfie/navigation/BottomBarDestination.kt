package com.mindera.alfie.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.mindera.alfie.R
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.navigation.arguments.shop.shopNavArgs
import com.mindera.alfie.core.navigation.arguments.wishlist.wishlistNavArgs
import com.mindera.alfie.core.ui.test.BAG_TAB
import com.mindera.alfie.core.ui.test.HOME_TAB
import com.mindera.alfie.core.ui.test.SHOP_TAB
import com.mindera.alfie.core.ui.test.WISHLIST_TAB
import com.mindera.alfie.designsystem.component.bottombar.BottomBarItem
import com.mindera.alfie.designsystem.component.bottombar.BottomBarItemState
import com.mindera.alfie.designsystem.component.bottombar.rememberBottomBarItemState
import com.mindera.alfie.feature.bag.destinations.BagScreenDestination
import com.mindera.alfie.feature.home.destinations.HomeScreenDestination
import com.mindera.alfie.feature.shop.destinations.ShopCategoryScreenDestination
import com.mindera.alfie.feature.shop.destinations.ShopScreenDestination
import com.mindera.alfie.feature.wishlist.destinations.WishlistScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.Direction
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import com.mindera.alfie.designsystem.R as RD
import com.mindera.alfie.designsystem.icons.AlfieIcons

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
            icon = AlfieIcons.Home,
            label = StringResource.fromId(id = R.string.bottom_bar_home),
            testTag = HOME_TAB,
            shouldSelect = { HomeScreenDestination == it },
            shouldRestore = { HomeScreenDestination == it }
        ),
        BottomBarDestination(
            direction = ShopScreenDestination(shopNavArgs()),
            state = rememberBottomBarItemState(),
            icon = AlfieIcons.LegacyStore,
            label = StringResource.fromId(id = R.string.bottom_bar_shop),
            testTag = SHOP_TAB,
            shouldSelect = { ShopScreenDestination == it || ShopCategoryScreenDestination == it },
            shouldRestore = { ShopScreenDestination == it }
        ),
        BottomBarDestination(
            direction = BagScreenDestination(),
            state = rememberBottomBarItemState(),
            icon = AlfieIcons.Bag,
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
            icon = AlfieIcons.Wishlist,
            label = StringResource.fromId(id = R.string.bottom_bar_wishlist),
            testTag = WISHLIST_TAB,
            shouldSelect = { WishlistScreenDestination == it },
            shouldRestore = { WishlistScreenDestination == it }
        ))
    }

    return persistentListOf<BottomBarDestination>().addAll(destinations)
}
