package au.com.alfie.ecomm.domain.usecase.productlist

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.productlist.ProductListRepository
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import javax.inject.Inject

class UpdateProductListLayoutModeUseCase @Inject constructor(
    private val productListRepository: ProductListRepository
) : UseCaseInteractor {

    suspend operator fun invoke(layoutMode: ProductListLayoutMode): UseCaseResult<Unit> =
        run(productListRepository.updateProductListLayoutMode(layoutMode))
}
