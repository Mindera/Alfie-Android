package au.com.alfie.ecomm.feature.bag

import au.com.alfie.ecomm.feature.bag.models.BagProductUi

sealed interface BagUiState {

    sealed class Data(open val bag: List<BagProductUi>) : BagUiState {

        data object Loading : Data(emptyList())

        data class Loaded(override val bag: List<BagProductUi>) : Data(bag)
    }

    data object Error : BagUiState
}