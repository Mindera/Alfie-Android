package com.mindera.alfie.data.product.service

import com.mindera.alfie.graphql.bff.GetProductDetailsQuery

internal interface ProductService {

    suspend fun getProduct(handle: String, platform: String): Result<GetProductDetailsQuery.Data>
}
