package com.mindera.alfie.data.productlist.repository

import com.mindera.alfie.data.datastore.user.UserPreferencesDataSource
import com.mindera.alfie.data.productlist.mapper.toDomain
import com.mindera.alfie.data.productlist.mapper.toProto
import com.mindera.alfie.data.productlist.service.ProductListService
import com.mindera.alfie.data.toRepositoryResult
import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.result.RepositoryResult
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
