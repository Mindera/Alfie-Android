package au.com.alfie.ecomm.feature.home

import au.com.alfie.ecomm.feature.home.model.HomeUI
import javax.inject.Inject

internal class HomeUIFactory @Inject constructor() {

    // TODO: get real data
    operator fun invoke() = HomeUI(userName = null, membershipDate = null)
}
