package com.mindera.alfie.feature.shop.brand.model

import androidx.compose.runtime.Stable
import com.mindera.alfie.feature.model.ApiErrorType
import kotlinx.collections.immutable.ImmutableList

@Stable
internal sealed class BrandUIState {

    @Stable
    data class Data(
        val entries: ImmutableList<BrandEntryUI>,
        val isLoading: Boolean
    ) : BrandUIState()

    @Stable
    data class Error(val errorType: ApiErrorType = ApiErrorType.Generic) : BrandUIState()
}
