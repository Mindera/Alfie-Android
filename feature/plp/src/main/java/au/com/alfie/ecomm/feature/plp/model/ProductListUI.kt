package au.com.alfie.ecomm.feature.plp.model

import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode

internal data class ProductListUI(
    val title: String,
    val resultCount: Int,
    val isLoadingMetadata: Boolean,
    val layoutMode: ProductListLayoutMode,
    val compactColumnCount: Int,
    val nonCompactColumnCount: Int,
    val wishlistIds: List<String>
) {

    companion object {
        val EMPTY = ProductListUI(
            title = "",
            resultCount = 0,
            isLoadingMetadata = true,
            layoutMode = ProductListLayoutMode.GRID,
            compactColumnCount = 1,
            nonCompactColumnCount = 1,
            wishlistIds = emptyList()
        )
    }
}
