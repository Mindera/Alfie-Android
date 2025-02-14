package au.com.alfie.ecomm.domain.usecase.bag

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.bag.BagProduct
import au.com.alfie.ecomm.repository.bag.BagRepository
import javax.inject.Inject

class GetBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    suspend operator fun invoke(): UseCaseResult<List<BagProduct>> =
        run(bagRepository.getBag())
}