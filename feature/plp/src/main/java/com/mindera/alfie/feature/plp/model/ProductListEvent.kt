package com.mindera.alfie.feature.plp.model

import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode

internal sealed interface ProductListEvent {

    data class OpenProduct(val productId: String) : ProductListEvent

    data object OpenFilters : ProductListEvent

    data class ChangeLayoutMode(val layoutMode: ProductListLayoutMode) : ProductListEvent
}
