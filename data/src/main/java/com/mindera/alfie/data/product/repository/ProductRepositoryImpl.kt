package com.mindera.alfie.data.product.repository

import com.mindera.alfie.data.product.mapper.toDomain
import com.mindera.alfie.data.product.service.ProductService
import com.mindera.alfie.data.toRepositoryResult
import com.mindera.alfie.repository.product.ProductRepository
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.result.RepositoryResult
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val productService: ProductService
) : ProductRepository {

    override suspend fun getProduct(handle: String, platform: String): RepositoryResult<Product> =
        productService.getProduct(handle = handle, platform = platform)
            .mapCatching { data ->
                data.productDetails?.productFragment?.toDomain()
                    ?: error("productDetails was null for handle=$handle")
            }
            .toRepositoryResult()
}
