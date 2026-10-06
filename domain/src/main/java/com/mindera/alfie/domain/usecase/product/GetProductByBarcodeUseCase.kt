package com.mindera.alfie.domain.usecase.product

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.product.ProductRepository
import com.mindera.alfie.repository.product.model.BarcodeMatch
import javax.inject.Inject

class GetProductByBarcodeUseCase @Inject constructor(
    private val productRepository: ProductRepository
) : UseCaseInteractor {

    suspend operator fun invoke(barcode: String): UseCaseResult<BarcodeMatch> =
        run(productRepository.getProductByBarcode(barcode = barcode))
}
