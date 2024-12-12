package au.com.alfie.ecomm.domain.usecase.product

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.product.ProductRepository
import au.com.alfie.ecomm.repository.product.model.Product
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) : UseCaseInteractor {

    suspend operator fun invoke(productId: String): UseCaseResult<Product> =
        run(productRepository.getProduct(productId = productId))
}
