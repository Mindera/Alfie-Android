package com.mindera.alfie.domain.usecase.productlist

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import javax.inject.Inject

class UpdateProductListLayoutModeUseCase @Inject constructor(
    private val productListRepository: ProductListRepository
) : UseCaseInteractor {

    suspend operator fun invoke(layoutMode: ProductListLayoutMode): UseCaseResult<Unit> =
        run(productListRepository.updateProductListLayoutMode(layoutMode))
}
