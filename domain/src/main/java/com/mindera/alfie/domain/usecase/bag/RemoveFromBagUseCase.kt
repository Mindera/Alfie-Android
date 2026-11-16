package com.mindera.alfie.domain.usecase.bag

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.bag.BagRepository
import javax.inject.Inject

/** Removes a whole Bag line, whatever its quantity. */
class RemoveFromBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    suspend operator fun invoke(lineId: String): UseCaseResult<Bag> =
        run(bagRepository.removeFromBag(lineId))
}
