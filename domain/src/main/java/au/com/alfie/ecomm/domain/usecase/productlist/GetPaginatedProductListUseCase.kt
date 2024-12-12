package au.com.alfie.ecomm.domain.usecase.productlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import au.com.alfie.ecomm.repository.productlist.ProductListRepository
import au.com.alfie.ecomm.repository.productlist.model.ProductListMetadata
import au.com.alfie.ecomm.repository.productlist.paging.ProductListPagingSource
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
        categoryId: String?,
        query: String?,
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
            categoryId = categoryId,
            query = query,
            metadataProvider = metadataProvider
        )
    }.flow
}
