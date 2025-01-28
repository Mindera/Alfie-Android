package au.com.alfie.ecomm.domain.usecase.bag

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.doOnResult
import au.com.alfie.ecomm.repository.bag.BagRepository
import au.com.alfie.ecomm.repository.product.ProductRepository
import javax.inject.Inject

class AddToBagUseCase @Inject constructor(
    private val bagRepository: BagRepository,
    private val productRepository: ProductRepository
) : UseCaseInteractor {

    suspend operator fun invoke(productId: String) =
        run(productRepository.getProduct(productId = productId))
            .doOnResult(
                onSuccess = {
                    bagRepository.addToBag(it)
                },
                onError = {
                    it
                }
            )
}
