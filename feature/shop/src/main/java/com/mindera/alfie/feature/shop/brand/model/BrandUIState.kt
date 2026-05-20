package com.mindera.alfie.feature.shop.brand.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.mindera.alfie.feature.shop.R
import kotlinx.collections.immutable.ImmutableList

@Stable
internal sealed class BrandUIState {

    @Stable
    data class Data(
        val entries: ImmutableList<BrandEntryUI>,
        val isLoading: Boolean
    ) : BrandUIState()

    @Stable
    data class Error(
        @StringRes val errorId: Int = R.string.shop_error_cannot_load_brands_list
    ) : BrandUIState()
}
