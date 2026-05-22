package com.mindera.alfie.feature.shop.model

import com.mindera.alfie.feature.model.ApiErrorType

internal sealed interface ShopUIState {

    data class Error(val errorType: ApiErrorType = ApiErrorType.Generic) : ShopUIState

    data class Data(val shopUI: ShopUI) : ShopUIState
}
