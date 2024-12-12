package au.com.alfie.ecomm.domain.usecase.productlist

import au.com.alfie.ecomm.repository.productlist.ProductListRepository
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import javax.inject.Inject

class GetProductListLayoutModeUseCase @Inject constructor(
    private val productListRepository: ProductListRepository
) {

    suspend operator fun invoke(): ProductListLayoutMode = productListRepository.getProductListLayoutMode()
}
