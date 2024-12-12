package au.com.alfie.ecomm.data.productlist.service

import au.com.alfie.ecomm.graphql.ProductListingQuery

internal interface ProductListService {

    suspend fun getProductList(
        offset: Int,
        limit: Int,
        categoryId: String?,
        query: String?
    ): Result<ProductListingQuery.Data>
}
