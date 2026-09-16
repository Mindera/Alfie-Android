package com.mindera.alfie.domain.usecase.bag

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.bag.BagRepository
import javax.inject.Inject

/**
 * Removes every unit of a product variant from the bag. Backs the Bag screen's line-level remove,
 * where a row stands for the whole quantity of that variant.
 */
class RemoveAllFromBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    suspend operator fun invoke(bagProduct: BagProduct): UseCaseResult<Boolean> =
        run(bagRepository.removeAllFromBag(bagProduct))
}
