package au.com.alfie.ecomm.repository.productlist

import au.com.alfie.ecomm.repository.productlist.model.ProductList
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import au.com.alfie.ecomm.repository.result.RepositoryResult

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
