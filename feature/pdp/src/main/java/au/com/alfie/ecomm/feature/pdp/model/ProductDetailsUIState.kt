package au.com.alfie.ecomm.feature.pdp.model

import au.com.alfie.ecomm.feature.pdp.ProductDetailsUIFactory

internal sealed interface ProductDetailsUIState {

    sealed class Data(open val details: ProductDetailsUI) : ProductDetailsUIState {

        data object Loading : Data(ProductDetailsUIFactory.LOADING)

        data class Loaded(override val details: ProductDetailsUI) : Data(details)
    }

    data object Error : ProductDetailsUIState
}
