package com.mindera.alfie.domain.usecase.productlist

import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import javax.inject.Inject

class GetProductListLayoutModeUseCase @Inject constructor(
    private val productListRepository: ProductListRepository
) {

    suspend operator fun invoke(): ProductListLayoutMode = productListRepository.getProductListLayoutMode()
}
