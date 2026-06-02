package com.mindera.alfie.feature.plp.model

import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.productlist.model.ProductSortOption

internal sealed interface ProductListEvent {

    data class OpenProduct(val productId: String) : ProductListEvent

    data object OpenFilters : ProductListEvent

    data class ChangeLayoutMode(val layoutMode: ProductListLayoutMode) : ProductListEvent

    data class ApplySort(val sort: ProductSortOption) : ProductListEvent

    data class ApplyFilters(val filters: ProductListFilter?) : ProductListEvent
}
