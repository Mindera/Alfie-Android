package au.com.alfie.ecomm.feature.account

import au.com.alfie.ecomm.feature.account.factory.MyAddressBook
import au.com.alfie.ecomm.feature.account.factory.MyDetails
import au.com.alfie.ecomm.feature.account.factory.MyOrders
import au.com.alfie.ecomm.feature.account.factory.SignOut
import au.com.alfie.ecomm.feature.account.factory.Wallet
import au.com.alfie.ecomm.feature.account.factory.Wishlist
import au.com.alfie.ecomm.feature.account.model.AccountUI

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
