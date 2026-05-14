package com.mindera.alfie.repository.navigation

import com.mindera.alfie.repository.navigation.model.HandleType
import com.mindera.alfie.repository.navigation.model.NavEntry
import com.mindera.alfie.repository.result.RepositoryResult

interface NavigationRepository {

    suspend fun getByParentId(parentId: Int): List<NavEntry>

    suspend fun getNavEntriesByHandle(handleType: HandleType): RepositoryResult<List<NavEntry>>
}
