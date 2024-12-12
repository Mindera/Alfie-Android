package au.com.alfie.ecomm.data.productlist.service

import au.com.alfie.ecomm.graphql.ProductListingQuery
import au.com.alfie.ecomm.network.extension.unwrap
import au.com.alfie.ecomm.network.graphql.GraphService
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
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
