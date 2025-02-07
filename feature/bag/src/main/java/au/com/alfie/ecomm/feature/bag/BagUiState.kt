package au.com.alfie.ecomm.feature.bag

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.feature.bag.models.BagProductUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Stable
internal sealed interface BagUiState {

    sealed class Data(open val bag: ImmutableList<BagProductUi>) : BagUiState {

        data object Loading : Data(listOf<BagProductUi>().toImmutableList())

        data class Loaded(override val bag: ImmutableList<BagProductUi>) : Data(bag)
    }

    data object Error : BagUiState
}