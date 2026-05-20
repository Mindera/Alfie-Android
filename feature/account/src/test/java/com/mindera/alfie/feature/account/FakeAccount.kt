package com.mindera.alfie.feature.account

import com.mindera.alfie.feature.account.factory.MyAddressBook
import com.mindera.alfie.feature.account.factory.MyDetails
import com.mindera.alfie.feature.account.factory.MyOrders
import com.mindera.alfie.feature.account.factory.SignOut
import com.mindera.alfie.feature.account.factory.Wallet
import com.mindera.alfie.feature.account.factory.Wishlist
import com.mindera.alfie.feature.account.model.AccountUI

internal val accountUILoaded = AccountUI(
    items = listOf(
        MyDetails,
        MyOrders,
        Wallet,
        MyAddressBook,
        Wishlist,
        SignOut
    )
)
