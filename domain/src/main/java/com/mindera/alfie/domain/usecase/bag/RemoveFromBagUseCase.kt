package com.mindera.alfie.domain.usecase.bag

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.bag.BagRepository
import javax.inject.Inject

class RemoveFromBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    suspend operator fun invoke(bagProduct: BagProduct): UseCaseResult<Boolean> =
        run(bagRepository.removeFromBag(bagProduct))
}
