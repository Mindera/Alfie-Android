package com.mindera.alfie.domain.usecase.productlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductListMetadata
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import com.mindera.alfie.repository.productlist.paging.ProductListPagingSource
import javax.inject.Inject
import kotlin.math.roundToInt

class GetPaginatedProductListUseCase @Inject constructor(
    private val repository: ProductListRepository
) {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 60
        private const val DEFAULT_PREFETCH_FRACTION = 0.3f
    }

    operator fun invoke(
        collectionHandle: String,
        filters: ProductListFilter?,
        sort: ProductSortOption,
        metadataProvider: (ProductListMetadata) -> Unit,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        prefetchFraction: Float = DEFAULT_PREFETCH_FRACTION
    ) = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            prefetchDistance = (pageSize * prefetchFraction).roundToInt(),
            enablePlaceholders = false
        )
    ) {
        ProductListPagingSource(
            productListRepository = repository,
            collectionHandle = collectionHandle,
            filters = filters,
            sort = sort,
            metadataProvider = metadataProvider
        )
    }.flow
}
