package com.mindera.alfie.domain.usecase.bag

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.bag.BagRepository
import javax.inject.Inject

class AddToBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    suspend operator fun invoke(
        variantId: String,
        productId: String?,
        quantity: Int = 1
    ): UseCaseResult<Bag> =
        run(bagRepository.addToBag(variantId = variantId, productId = productId, quantity = quantity))
}
