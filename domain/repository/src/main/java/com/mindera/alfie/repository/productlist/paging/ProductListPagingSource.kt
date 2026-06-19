package com.mindera.alfie.repository.productlist.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductListMetadata
import com.mindera.alfie.repository.productlist.model.ProductListQuerySource
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import com.mindera.alfie.repository.result.fold

class ProductListPagingSource(
    private val productListRepository: ProductListRepository,
    private val source: ProductListQuerySource,
    private val filters: ProductListFilter?,
    private val sort: ProductSortOption,
    private val metadataProvider: (ProductListMetadata) -> Unit
) : PagingSource<String, ProductListEntry>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ProductListEntry> {
        val after = params.key
        val response = productListRepository.getProductList(
            after = after,
            source = source,
            filters = filters,
            sort = sort,
            limit = params.loadSize
        )

        return response.fold(
            onSuccess = { data ->
                metadataProvider(ProductListMetadata(totalResults = data.pagination.totalCount))

                LoadResult.Page(
                    data = data.products,
                    prevKey = null,
                    nextKey = data.pagination.endCursor.takeIf { data.pagination.hasNextPage }
                )
            },
            onError = {
                LoadResult.Error(it)
            }
        )
    }

    override fun getRefreshKey(state: PagingState<String, ProductListEntry>): String? = null

    override val keyReuseSupported: Boolean = false
}
