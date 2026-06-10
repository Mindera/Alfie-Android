package com.mindera.alfie.data.product.service

import com.apollographql.apollo.ApolloClient
import com.mindera.alfie.graphql.bff.GetProductDetailsQuery
import com.mindera.alfie.network.di.NewClient
import com.mindera.alfie.network.extension.unwrap
import com.mindera.alfie.network.graphql.GraphService
import javax.inject.Inject

internal class ProductServiceImpl @Inject constructor(
    @NewClient apolloClient: ApolloClient
) : GraphService(apolloClient), ProductService {

    override suspend fun getProduct(
        handle: String,
        platform: String
    ): Result<GetProductDetailsQuery.Data> =
        query(GetProductDetailsQuery(handle = handle, platform = platform)).unwrap()
}
