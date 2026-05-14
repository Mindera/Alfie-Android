package com.mindera.alfie.repository.productlist

import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.result.RepositoryResult

interface ProductListRepository {

    /**
     * @param offset starting position within the list
     * @param limit number of results per page
     * @param categoryId filter by category ID
     * @param query filter by custom query
     */
    suspend fun getProductList(
        offset: Int,
        limit: Int,
        categoryId: String?,
        query: String?
    ): RepositoryResult<ProductList>

    suspend fun updateProductListLayoutMode(layoutMode: ProductListLayoutMode): RepositoryResult<Unit>

    suspend fun getProductListLayoutMode(): ProductListLayoutMode
}
