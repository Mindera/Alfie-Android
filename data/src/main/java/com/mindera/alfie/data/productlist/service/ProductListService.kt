package com.mindera.alfie.data.productlist.service

import com.mindera.alfie.graphql.bff.ProductListQuery
import com.mindera.alfie.graphql.bff.type.ProductFilterInput
import com.mindera.alfie.graphql.bff.type.ProductSortEnum
import java.util.Optional

internal interface ProductListService {

    suspend fun getProductList(
        after: String?,
        collectionHandle: String,
        filters: Optional<ProductFilterInput>,
        sort: ProductSortEnum,
        limit: Int
    ): Result<ProductListQuery.Data>
}
