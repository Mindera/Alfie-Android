package au.com.alfie.ecomm.repository.product

import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.RepositoryResult

interface ProductRepository {

    suspend fun getProduct(productId: String): RepositoryResult<Product>
}
