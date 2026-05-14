package com.mindera.alfie.data.navigation.remote.service

import com.mindera.alfie.graphql.NavEntriesByHandleQuery
import com.mindera.alfie.network.extension.unwrap
import com.mindera.alfie.network.graphql.GraphService
import com.apollographql.apollo3.ApolloClient
import javax.inject.Inject

internal class RemoteNavigationServiceImpl @Inject constructor(
    apolloClient: ApolloClient
) : GraphService(apolloClient), RemoteNavigationService {

    override suspend fun getNavEntriesByHandle(handle: String): Result<NavEntriesByHandleQuery.Data> =
        query(NavEntriesByHandleQuery(handle = handle)).unwrap()
}
