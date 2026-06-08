package com.mindera.alfie.data.productlist.repository

import com.mindera.alfie.data.datastore.user.UserPreferencesDataSource
import com.mindera.alfie.data.productlist.mapper.toDomain
import com.mindera.alfie.data.productlist.mapper.toProto
import com.mindera.alfie.data.productlist.service.ProductListService
import com.mindera.alfie.data.productlist.service.toGraphQL
import com.mindera.alfie.data.toRepositoryResult
import com.mindera.alfie.repository.productlist.ProductListRepository
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import com.mindera.alfie.repository.result.RepositoryResult
import javax.inject.Inject

internal class ProductListRepositoryImpl @Inject constructor(
    private val productListService: ProductListService,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : ProductListRepository {

    override suspend fun getProductList(
        after: String?,
        collectionHandle: String,
        filters: ProductListFilter?,
        sort: ProductSortOption,
        limit: Int
    ): RepositoryResult<ProductList> =
        productListService.getProductList(
            after = after,
            collectionHandle = collectionHandle,
            filters = filters.toGraphQL(),
            sort = sort.toGraphQL(),
            limit = limit
        )
            .mapCatching { data ->
                data.productList.toDomain()
            }
            .toRepositoryResult()

    override suspend fun updateProductListLayoutMode(layoutMode: ProductListLayoutMode): RepositoryResult<Unit> = runCatching {
        userPreferencesDataSource.updateProductListLayoutMode(layoutMode.toProto())
    }.toRepositoryResult()

    override suspend fun getProductListLayoutMode(): ProductListLayoutMode = userPreferencesDataSource.getProductListLayoutMode().toDomain()
}
