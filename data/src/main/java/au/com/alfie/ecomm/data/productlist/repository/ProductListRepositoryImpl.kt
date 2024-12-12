package au.com.alfie.ecomm.data.productlist.repository

import au.com.alfie.ecomm.data.datastore.user.UserPreferencesDataSource
import au.com.alfie.ecomm.data.productlist.mapper.toDomain
import au.com.alfie.ecomm.data.productlist.mapper.toProto
import au.com.alfie.ecomm.data.productlist.service.ProductListService
import au.com.alfie.ecomm.data.toRepositoryResult
import au.com.alfie.ecomm.repository.productlist.ProductListRepository
import au.com.alfie.ecomm.repository.productlist.model.ProductList
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import au.com.alfie.ecomm.repository.result.RepositoryResult
import javax.inject.Inject

internal class ProductListRepositoryImpl @Inject constructor(
    private val productListService: ProductListService,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : ProductListRepository {

    override suspend fun getProductList(
        offset: Int,
        limit: Int,
        categoryId: String?,
        query: String?
    ): RepositoryResult<ProductList> =
        productListService.getProductList(
            offset = offset,
            limit = limit,
            categoryId = categoryId,
            query = query
        )
            .mapCatching { data ->
                requireNotNull(data.productListing)
                data.productListing!!.toDomain()
            }
            .toRepositoryResult()

    override suspend fun updateProductListLayoutMode(layoutMode: ProductListLayoutMode): RepositoryResult<Unit> = runCatching {
        userPreferencesDataSource.updateProductListLayoutMode(layoutMode.toProto())
    }.toRepositoryResult()

    override suspend fun getProductListLayoutMode(): ProductListLayoutMode = userPreferencesDataSource.getProductListLayoutMode().toDomain()
}
