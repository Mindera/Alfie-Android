package au.com.alfie.ecomm.domain.usecase.productlist

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.productlist.ProductListRepository
import au.com.alfie.ecomm.repository.productlist.model.ProductList
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(
    private val productListRepository: ProductListRepository
) : UseCaseInteractor {

    suspend operator fun invoke(
        offset: Int,
        limit: Int,
        categoryId: String?,
        query: String?
    ): UseCaseResult<ProductList> =
        run(
            productListRepository.getProductList(
                offset = offset,
                limit = limit,
                categoryId = categoryId,
                query = query
            )
        )
}
