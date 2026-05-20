package com.mindera.alfie.data.navigation.remote.service

import com.mindera.alfie.graphql.NavEntriesByHandleQuery

internal interface RemoteNavigationService {

    suspend fun getNavEntriesByHandle(handle: String): Result<NavEntriesByHandleQuery.Data>
}
