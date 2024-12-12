package au.com.alfie.ecomm.feature.plp.model

import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode

internal sealed interface ProductListEvent {

    data class OpenProduct(val productId: String) : ProductListEvent

    data object OpenFilters : ProductListEvent

    data class ChangeLayoutMode(val layoutMode: ProductListLayoutMode) : ProductListEvent
}
