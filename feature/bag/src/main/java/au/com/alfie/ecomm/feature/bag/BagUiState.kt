package au.com.alfie.ecomm.feature.bag

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.feature.bag.models.BagProductUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Stable
internal sealed interface BagUiState {

    sealed interface Data : BagUiState {

        data object Loading : Data

        data class Loaded(val bag: ImmutableList<BagProductUi>) : Data

        data object Empty : Data
    }

    data object Error : BagUiState
}