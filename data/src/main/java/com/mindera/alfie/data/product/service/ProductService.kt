package com.mindera.alfie.data.product.service

import com.mindera.alfie.graphql.ProductQuery

internal interface ProductService {

    suspend fun getProduct(productId: String): Result<ProductQuery.Data>
}
