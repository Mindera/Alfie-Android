package com.mindera.alfie.data.productlist.service

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.mindera.alfie.graphql.ProductListingQuery
import com.mindera.alfie.network.extension.unwrap
import com.mindera.alfie.network.graphql.GraphService
import javax.inject.Inject

internal class ProductListServiceImpl @Inject constructor(
    apolloClient: ApolloClient
) : GraphService(apolloClient), ProductListService {

    override suspend fun getProductList(
        offset: Int,
        limit: Int,
        categoryId: String?,
        query: String?
    ): Result<ProductListingQuery.Data> =
        query(
            ProductListingQuery(
                offset = offset,
                limit = limit,
                categoryId = Optional.presentIfNotNull(categoryId),
                query = Optional.presentIfNotNull(query)
            )
        ).unwrap()
}
