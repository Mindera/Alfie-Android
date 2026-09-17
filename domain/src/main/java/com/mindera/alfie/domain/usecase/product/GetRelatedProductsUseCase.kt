package com.mindera.alfie.domain.usecase.product

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.product.ProductRepository
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import javax.inject.Inject

class GetRelatedProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) : UseCaseInteractor {

    suspend operator fun invoke(
        handle: String,
        limit: Int = DEFAULT_LIMIT
    ): UseCaseResult<List<ProductListEntry>> =
        run(productRepository.getRelatedProducts(handle = handle, limit = limit))

    private companion object {
        /** The PDP recommendations grid shows three rows of two (Figma "Recommendations"). */
        const val DEFAULT_LIMIT = 6
    }
}
