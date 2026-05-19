package com.mindera.alfie.data.product.service

import com.apollographql.apollo3.ApolloClient
import com.mindera.alfie.graphql.ProductQuery
import com.mindera.alfie.network.extension.unwrap
import com.mindera.alfie.network.graphql.GraphService
import javax.inject.Inject

internal class ProductServiceImpl @Inject constructor(
    apolloClient: ApolloClient
) : GraphService(apolloClient), ProductService {

    override suspend fun getProduct(productId: String): Result<ProductQuery.Data> =
        query(ProductQuery(productId = productId)).unwrap()
}
