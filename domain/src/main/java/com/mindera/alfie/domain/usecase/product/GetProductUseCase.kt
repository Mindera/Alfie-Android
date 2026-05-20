package com.mindera.alfie.domain.usecase.product

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.product.ProductRepository
import com.mindera.alfie.repository.product.model.Product
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) : UseCaseInteractor {

    suspend operator fun invoke(productId: String): UseCaseResult<Product> =
        run(productRepository.getProduct(productId = productId))
}
