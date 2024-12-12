package au.com.alfie.ecomm.feature.account.factory

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.feature.account.model.AccountUI
import au.com.alfie.ecomm.feature.account.model.NavigationButtonUI
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
