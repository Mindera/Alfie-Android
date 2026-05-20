package com.mindera.alfie.feature.account.factory

import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.wishlist.wishlistNavArgs
import com.mindera.alfie.core.ui.test.ACCOUNT_ADDRESS_BOOK_SECTION
import com.mindera.alfie.core.ui.test.ACCOUNT_MY_DETAILS_SECTION
import com.mindera.alfie.core.ui.test.ACCOUNT_MY_ORDERS_SECTION
import com.mindera.alfie.core.ui.test.ACCOUNT_SIGN_OUT_SECTION
import com.mindera.alfie.core.ui.test.ACCOUNT_WALLET_SECTION
import com.mindera.alfie.core.ui.test.ACCOUNT_WISHLIST_SECTION
import com.mindera.alfie.feature.account.R
import com.mindera.alfie.feature.account.model.NavigationButtonUI
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.designsystem.R as RD

// TODO Remove this value when all options are implemented
private val noActionImplemented = object : UIEvent.Custom {}

internal val MyDetails = NavigationButtonUI(
    title = R.string.account_my_details,
    icon = RD.drawable.ic_action_user,
    uiEvent = noActionImplemented,
    testTag = ACCOUNT_MY_DETAILS_SECTION
)

internal val MyOrders = NavigationButtonUI(
    title = R.string.account_my_orders,
    icon = RD.drawable.ic_informational_store,
    uiEvent = noActionImplemented,
    testTag = ACCOUNT_MY_ORDERS_SECTION
)

internal val Wallet = NavigationButtonUI(
    title = R.string.account_wallet,
    icon = RD.drawable.ic_informational_credit_card,
    uiEvent = noActionImplemented,
    testTag = ACCOUNT_WALLET_SECTION
)

internal val MyAddressBook = NavigationButtonUI(
    title = R.string.account_my_address_book,
    icon = RD.drawable.ic_informational_location,
    uiEvent = noActionImplemented,
    testTag = ACCOUNT_ADDRESS_BOOK_SECTION
)

internal val Wishlist = NavigationButtonUI(
    title = R.string.account_wishlist,
    icon = RD.drawable.ic_action_heart_outline,
    uiEvent = UIEvent.Base.NavigateToScreen(screen = Screen.Wishlist(args = wishlistNavArgs())),
    testTag = ACCOUNT_WISHLIST_SECTION
)

internal val SignOut = NavigationButtonUI(
    title = R.string.account_sign_out,
    icon = RD.drawable.ic_action_log_out,
    uiEvent = noActionImplemented,
    testTag = ACCOUNT_SIGN_OUT_SECTION
)
