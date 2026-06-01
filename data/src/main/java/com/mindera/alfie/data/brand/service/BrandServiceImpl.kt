package com.mindera.alfie.data.brand.service

import com.apollographql.apollo.ApolloClient
import com.mindera.alfie.graphql.BrandsQuery
import com.mindera.alfie.network.di.LegacyClient
import com.mindera.alfie.network.extension.unwrap
import com.mindera.alfie.network.graphql.GraphService
import javax.inject.Inject

internal class BrandServiceImpl @Inject constructor(
    @LegacyClient apolloClient: ApolloClient
) : GraphService(apolloClient), BrandService {

    override suspend fun getBrands(): Result<BrandsQuery.Data> = query(BrandsQuery()).unwrap()
}
