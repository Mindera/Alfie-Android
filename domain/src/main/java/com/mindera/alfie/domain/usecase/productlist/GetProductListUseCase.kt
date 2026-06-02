package com.mindera.alfie.domain.usecase.productlist

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(
    private val productListRepository: ProductListRepository
) : UseCaseInteractor {

    suspend operator fun invoke(
        after: String?,
        collectionHandle: String,
        filters: ProductListFilter?,
        sort: ProductSortOption,
        limit: Int
    ): UseCaseResult<ProductList> =
        run(
            productListRepository.getProductList(
                after = after,
                collectionHandle = collectionHandle,
                filters = filters,
                sort = sort,
                limit = limit
            )
        )
}
