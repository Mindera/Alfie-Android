package com.mindera.alfie.feature.plp.model

import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.productlist.model.ProductSortOption

internal data class ProductListUI(
    val resultCount: Int,
    val isLoadingMetadata: Boolean,
    val layoutMode: ProductListLayoutMode,
    val compactColumnCount: Int,
    val nonCompactColumnCount: Int,
    val wishlistIds: Set<String>,
    val selectedSort: ProductSortOption,
    val selectedFilters: ProductListFilter?,
    val showRefine: Boolean
) {

    companion object {
        val EMPTY = ProductListUI(
            resultCount = 0,
            isLoadingMetadata = true,
            layoutMode = ProductListLayoutMode.GRID,
            compactColumnCount = 1,
            nonCompactColumnCount = 1,
            wishlistIds = emptySet(),
            selectedSort = ProductSortOption.RECOMMENDED,
            selectedFilters = null,
            showRefine = false
        )
    }
}
