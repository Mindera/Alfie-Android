package au.com.alfie.ecomm.data.product.service

import au.com.alfie.ecomm.graphql.ProductQuery
import au.com.alfie.ecomm.network.extension.unwrap
import au.com.alfie.ecomm.network.graphql.GraphService
import com.apollographql.apollo3.ApolloClient
import javax.inject.Inject

internal class ProductServiceImpl @Inject constructor(
    apolloClient: ApolloClient
) : GraphService(apolloClient), ProductService {

    override suspend fun getProduct(productId: String): Result<ProductQuery.Data> =
        query(ProductQuery(productId = productId)).unwrap()
}
