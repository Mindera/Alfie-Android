package com.mindera.alfie.repository.product

import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.result.RepositoryResult

interface ProductRepository {

    suspend fun getProduct(productId: String): RepositoryResult<Product>
}
