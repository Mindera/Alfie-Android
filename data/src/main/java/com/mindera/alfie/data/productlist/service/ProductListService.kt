package com.mindera.alfie.data.productlist.service

import com.mindera.alfie.graphql.bff.ProductListQuery

internal interface ProductListService {

    suspend fun getProductList(
        after: String?,
        collectionHandle: String,
        filters: com.apollographql.apollo.api.Optional<com.mindera.alfie.graphql.bff.type.ProductFilterInput>,
        sort: com.mindera.alfie.graphql.bff.type.ProductSortEnum,
        limit: Int
    ): Result<ProductListQuery.Data>
}
