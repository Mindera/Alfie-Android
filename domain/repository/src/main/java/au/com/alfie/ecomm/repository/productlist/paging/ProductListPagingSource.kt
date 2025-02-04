package au.com.alfie.ecomm.repository.productlist.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import au.com.alfie.ecomm.repository.productlist.ProductListRepository
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntry
import au.com.alfie.ecomm.repository.productlist.model.ProductListMetadata
import au.com.alfie.ecomm.repository.result.fold

class ProductListPagingSource(
    private val productListRepository: ProductListRepository,
    private val categoryId: String?,
    private val query: String?,
    private val metadataProvider: (ProductListMetadata) -> Unit
) : PagingSource<Int, ProductListEntry>() {

    companion object {
        private const val INITIAL_OFFSET = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductListEntry> {
        val offset = params.key ?: INITIAL_OFFSET
        val response = productListRepository.getProductList(
            offset = offset,
            limit = params.loadSize,
            categoryId = categoryId,
            query = query
        )

        return response.fold(
            onSuccess = { data ->
                val metadata = ProductListMetadata(
                    title = data.title,
                    totalResults = data.pagination.total
                )
                metadataProvider(metadata)

                LoadResult.Page(
                    data = data.products,
                    nextKey = data.pagination.nextPage,
                    prevKey = data.pagination.previousPage
                )
            },
            onError = {
                LoadResult.Error(it)
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ProductListEntry>): Int? = null
}
