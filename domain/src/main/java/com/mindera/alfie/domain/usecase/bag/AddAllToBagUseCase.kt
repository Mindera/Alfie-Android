package com.mindera.alfie.domain.usecase.bag

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.bag.BagRepository
import javax.inject.Inject

/**
 * Puts every unit of a product variant back into the bag at a given position. Backs the Bag
 * screen's undo, the counterpart to [RemoveAllFromBagUseCase].
 */
class AddAllToBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    suspend operator fun invoke(bagProduct: BagProduct, quantity: Int, index: Int): UseCaseResult<Boolean> =
        run(bagRepository.addAllToBag(bagProduct = bagProduct, quantity = quantity, index = index))
}
