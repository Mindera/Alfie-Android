package au.com.alfie.ecomm.feature.shop.brand.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.feature.shop.R
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
