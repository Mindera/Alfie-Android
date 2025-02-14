package au.com.alfie.ecomm.domain.usecase.bag

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.domain.doOnResult
import au.com.alfie.ecomm.repository.bag.BagProduct
import au.com.alfie.ecomm.repository.bag.BagRepository
import javax.inject.Inject

class AddToBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    suspend operator fun invoke(productId: String, variantSku: String) =
        run(bagRepository.addToBag(BagProduct(productId = productId, variantSku = variantSku))).doOnResult(
            onSuccess = { UseCaseResult.Success(it) },
            onError = { UseCaseResult.Error(it) }
        )
}
