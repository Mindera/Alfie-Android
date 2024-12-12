package au.com.alfie.ecomm.data.product.service

import au.com.alfie.ecomm.graphql.ProductQuery

internal interface ProductService {

    suspend fun getProduct(productId: String): Result<ProductQuery.Data>
}
