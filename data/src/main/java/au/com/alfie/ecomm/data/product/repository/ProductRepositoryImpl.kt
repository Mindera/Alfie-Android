package au.com.alfie.ecomm.data.product.repository

import au.com.alfie.ecomm.data.product.mapper.toDomain
import au.com.alfie.ecomm.data.product.service.ProductService
import au.com.alfie.ecomm.data.toRepositoryResult
import au.com.alfie.ecomm.repository.product.ProductRepository
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.RepositoryResult
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val productService: ProductService
) : ProductRepository {

    override suspend fun getProduct(productId: String): RepositoryResult<Product> =
        productService.getProduct(productId = productId)
            .mapCatching { data -> data.product?.productInfo?.toDomain() ?: throw NullPointerException("The product field in Data was null") }
            .toRepositoryResult()
}
