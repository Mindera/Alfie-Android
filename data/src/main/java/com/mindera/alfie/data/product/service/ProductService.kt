package com.mindera.alfie.data.product.service

import com.mindera.alfie.graphql.bff.GetProductDetailsQuery
import com.mindera.alfie.graphql.bff.GetRelatedProductsQuery

internal interface ProductService {

    suspend fun getProduct(handle: String): Result<GetProductDetailsQuery.Data>

    suspend fun getRelatedProducts(handle: String, limit: Int): Result<GetRelatedProductsQuery.Data>
}
