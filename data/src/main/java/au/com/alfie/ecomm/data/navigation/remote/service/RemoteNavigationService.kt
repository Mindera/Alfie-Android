package au.com.alfie.ecomm.data.navigation.remote.service

import au.com.alfie.ecomm.graphql.NavEntriesByHandleQuery

internal interface RemoteNavigationService {

    suspend fun getNavEntriesByHandle(handle: String): Result<NavEntriesByHandleQuery.Data>
}
