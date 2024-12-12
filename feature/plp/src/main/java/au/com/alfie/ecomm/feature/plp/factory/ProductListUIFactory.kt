package au.com.alfie.ecomm.feature.plp.factory

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.feature.plp.model.ProductListUI
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import au.com.alfie.ecomm.repository.productlist.model.ProductListMetadata
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ProductListUIFactory @Inject constructor(
    private val dispatcher: DispatcherProvider
) {

    companion object {
        private const val COLUMN_COUNT_COMPACT_GRID = 2
        private const val COLUMN_COUNT_NON_COMPACT_GRID = 3
        private const val COLUMN_COUNT_COMPACT_COLUMN = 1
        private const val COLUMN_COUNT_NON_COMPACT_COLUMN = 2
    }

    suspend operator fun invoke(
        productListUI: ProductListUI,
        layoutMode: ProductListLayoutMode
    ): ProductListUI = withContext(dispatcher.default()) {
        val (compactColumnCount, nonCompactColumnCount) = when (layoutMode) {
            ProductListLayoutMode.GRID -> COLUMN_COUNT_COMPACT_GRID to COLUMN_COUNT_NON_COMPACT_GRID
            ProductListLayoutMode.COLUMN -> COLUMN_COUNT_COMPACT_COLUMN to COLUMN_COUNT_NON_COMPACT_COLUMN
        }
        productListUI.copy(
            layoutMode = layoutMode,
            compactColumnCount = compactColumnCount,
            nonCompactColumnCount = nonCompactColumnCount
        )
    }

    suspend operator fun invoke(
        productListUI: ProductListUI,
        metadata: ProductListMetadata
    ): ProductListUI = withContext(dispatcher.default()) {
        productListUI.copy(
            title = metadata.title,
            resultCount = metadata.totalResults,
            isLoadingMetadata = false
        )
    }
}
