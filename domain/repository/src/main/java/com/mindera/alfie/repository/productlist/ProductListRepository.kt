package com.mindera.alfie.repository.productlist

import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.productlist.model.ProductListQuerySource
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import com.mindera.alfie.repository.result.RepositoryResult

interface ProductListRepository {

    /**
     * @param after cursor for the next page; null for the first page
     * @param source the BFF query backing the list — a collection handle or a search term
     * @param filters optional product filters
     * @param sort sort order, defaults to [ProductSortOption.RECOMMENDED]
     * @param limit number of products per page
     */
    suspend fun getProductList(
        after: String?,
        source: ProductListQuerySource,
        filters: ProductListFilter?,
        sort: ProductSortOption,
        limit: Int
    ): RepositoryResult<ProductList>

    suspend fun updateProductListLayoutMode(layoutMode: ProductListLayoutMode): RepositoryResult<Unit>

    suspend fun getProductListLayoutMode(): ProductListLayoutMode
}
