package com.mindera.alfie.data.productlist.service

import com.mindera.alfie.graphql.ProductListingQuery

internal interface ProductListService {

    suspend fun getProductList(
        offset: Int,
        limit: Int,
        categoryId: String?,
        query: String?
    ): Result<ProductListingQuery.Data>
}
