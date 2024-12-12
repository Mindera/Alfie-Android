package au.com.alfie.ecomm.feature.shop.category.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.feature.shop.R
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
    data class Error(
        @StringRes
        val errorId: Int = R.string.shop_error_cannot_load_categories_list
    ) : CategoryUIState
}
