package au.com.alfie.ecomm.data.brand.service

import au.com.alfie.ecomm.graphql.BrandsQuery
import au.com.alfie.ecomm.network.extension.unwrap
import au.com.alfie.ecomm.network.graphql.GraphService
import com.apollographql.apollo3.ApolloClient
import javax.inject.Inject

internal class BrandServiceImpl @Inject constructor(
    apolloClient: ApolloClient
) : GraphService(apolloClient), BrandService {

    override suspend fun getBrands(): Result<BrandsQuery.Data> = query(BrandsQuery()).unwrap()
}
