package com.mindera.alfie.feature.shop.category.model

import androidx.compose.runtime.Stable
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.feature.model.ApiErrorType
import kotlinx.collections.immutable.ImmutableList

@Stable
internal sealed interface CategoryUIState {

    @Stable
    data class Data(
        val title: StringResource,
        val entries: ImmutableList<CategoryEntryUI>,
        val isLoading: Boolean
    ) : CategoryUIState

    @Stable
    data class Error(val errorType: ApiErrorType = ApiErrorType.Generic) : CategoryUIState
}
