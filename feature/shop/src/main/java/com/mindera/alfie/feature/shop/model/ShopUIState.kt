package com.mindera.alfie.feature.shop.model

internal sealed interface ShopUIState {

    data object Error : ShopUIState

    data class Data(val shopUI: ShopUI) : ShopUIState
}
