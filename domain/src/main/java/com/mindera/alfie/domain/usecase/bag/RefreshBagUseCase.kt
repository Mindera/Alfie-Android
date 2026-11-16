package com.mindera.alfie.domain.usecase.bag

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.bag.BagRepository
import javax.inject.Inject

/** Fetches the Cart again; the result also reaches every [GetBagUseCase] collector. */
class RefreshBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    suspend operator fun invoke(): UseCaseResult<Bag> = run(bagRepository.refreshBag())
}
