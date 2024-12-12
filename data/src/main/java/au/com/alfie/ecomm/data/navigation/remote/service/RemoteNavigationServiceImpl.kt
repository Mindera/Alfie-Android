package au.com.alfie.ecomm.data.navigation.remote.service

import au.com.alfie.ecomm.graphql.NavEntriesByHandleQuery
import au.com.alfie.ecomm.network.extension.unwrap
import au.com.alfie.ecomm.network.graphql.GraphService
import com.apollographql.apollo3.ApolloClient
import javax.inject.Inject

internal class RemoteNavigationServiceImpl @Inject constructor(
    apolloClient: ApolloClient
) : GraphService(apolloClient), RemoteNavigationService {

    override suspend fun getNavEntriesByHandle(handle: String): Result<NavEntriesByHandleQuery.Data> =
        query(NavEntriesByHandleQuery(handle = handle)).unwrap()
}
