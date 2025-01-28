package au.com.alfie.ecomm.domain.usecase.bag

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.bag.BagRepository
import au.com.alfie.ecomm.repository.product.model.Product
import javax.inject.Inject

class GetBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    suspend operator fun invoke(): UseCaseResult<List<Product>> =
        run(bagRepository.getBag())
}