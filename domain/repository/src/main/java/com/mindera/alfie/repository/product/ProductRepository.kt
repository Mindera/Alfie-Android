package com.mindera.alfie.repository.product

import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.result.RepositoryResult

interface ProductRepository {

    suspend fun getProduct(handle: String): RepositoryResult<Product>

    suspend fun getRelatedProducts(handle: String, limit: Int): RepositoryResult<List<ProductListEntry>>
}
