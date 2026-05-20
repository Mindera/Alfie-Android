package com.mindera.alfie.feature.home

import com.mindera.alfie.feature.home.model.HomeUI
import javax.inject.Inject

internal class HomeUIFactory @Inject constructor() {

    // TODO: get real data
    operator fun invoke() = HomeUI(userName = null, membershipDate = null)
}
