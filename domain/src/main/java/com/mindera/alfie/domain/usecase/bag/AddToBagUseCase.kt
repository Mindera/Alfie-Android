package com.mindera.alfie.domain.usecase.bag

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.bag.BagRepository
import javax.inject.Inject

class AddToBagUseCase @Inject constructor(
    private val bagRepository: BagRepository
) : UseCaseInteractor {

    suspend operator fun invoke(
        productId: String,
        variantSku: String
    ) = run(
        bagRepository.addToBag(
            BagProduct(
                productId = productId,
                variantSku = variantSku
            )
        )
    ).doOnResult(
        onSuccess = { UseCaseResult.Success(it) },
        onError = { UseCaseResult.Error(it) }
    )
}
