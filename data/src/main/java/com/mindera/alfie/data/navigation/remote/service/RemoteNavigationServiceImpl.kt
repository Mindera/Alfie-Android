package com.mindera.alfie.data.navigation.remote.service

import com.apollographql.apollo.ApolloClient
import com.mindera.alfie.graphql.bff.MainMenuQuery
import com.mindera.alfie.network.extension.unwrap
import com.mindera.alfie.network.graphql.GraphService
import javax.inject.Inject

internal class RemoteNavigationServiceImpl @Inject constructor(
    apolloClient: ApolloClient
) : GraphService(apolloClient), RemoteNavigationService {

    override suspend fun getNavEntriesByHandle(handle: String): Result<MainMenuQuery.Data> =
        query(MainMenuQuery(handle = handle)).unwrap()
}
