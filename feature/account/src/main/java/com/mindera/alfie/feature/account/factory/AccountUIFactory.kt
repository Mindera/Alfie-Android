package com.mindera.alfie.feature.account.factory

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.feature.account.model.AccountUI
import com.mindera.alfie.feature.account.model.NavigationButtonUI
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AccountUIFactory @Inject constructor(
    private val dispatcher: DispatcherProvider
) {

    suspend operator fun invoke(): AccountUI = withContext(dispatcher.default()) {
        AccountUI(
            items = buildMenuItems()
        )
    }

    private fun buildMenuItems(): List<NavigationButtonUI> = buildList {
        add(MyDetails)
        add(MyOrders)
        add(Wallet)
        add(MyAddressBook)
        add(Wishlist)
        add(SignOut)
    }
}
