package com.mindera.alfie.domain.usecase.productlist

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.ProductList
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(
    private val productListRepository: ProductListRepository
) : UseCaseInteractor {

    suspend operator fun invoke(
        offset: Int,
        limit: Int,
        categoryId: String?,
        query: String?
    ): UseCaseResult<ProductList> =
        run(
            productListRepository.getProductList(
                offset = offset,
                limit = limit,
                categoryId = categoryId,
                query = query
            )
        )
}
