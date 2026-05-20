package com.mindera.alfie.feature.plp.model

import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode

internal data class ProductListUI(
    val title: String,
    val resultCount: Int,
    val isLoadingMetadata: Boolean,
    val layoutMode: ProductListLayoutMode,
    val compactColumnCount: Int,
    val nonCompactColumnCount: Int,
    val wishlistIds: Set<String>
) {

    companion object {
        val EMPTY = ProductListUI(
            title = "",
            resultCount = 0,
            isLoadingMetadata = true,
            layoutMode = ProductListLayoutMode.GRID,
            compactColumnCount = 1,
            nonCompactColumnCount = 1,
            wishlistIds = emptySet()
        )
    }
}
