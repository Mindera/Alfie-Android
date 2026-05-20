package com.mindera.alfie.data.navigation.repository

import com.mindera.alfie.data.database.navigation.NavigationEntryDao
import com.mindera.alfie.data.database.navigation.model.NavigationEntryEntity
import com.mindera.alfie.data.navigation.cache.mapper.toDomain
import com.mindera.alfie.data.navigation.remote.mapper.toEntity
import com.mindera.alfie.data.navigation.remote.service.RemoteNavigationService
import com.mindera.alfie.data.toRepositoryResult
import com.mindera.alfie.graphql.NavEntriesByHandleQuery
import com.mindera.alfie.repository.navigation.NavigationRepository
import com.mindera.alfie.repository.navigation.model.HandleType
import com.mindera.alfie.repository.navigation.model.NavEntry
import com.mindera.alfie.repository.result.RepositoryResult
import javax.inject.Inject

internal class NavigationRepositoryImpl @Inject constructor(
    private val remoteNavigationService: RemoteNavigationService,
    private val navigationEntryDao: NavigationEntryDao
) : NavigationRepository {

    override suspend fun getByParentId(parentId: Int): List<NavEntry> =
        navigationEntryDao.getByParentId(parentId = parentId).toDomain()

    override suspend fun getNavEntriesByHandle(handleType: HandleType): RepositoryResult<List<NavEntry>> =
        remoteNavigationService.getNavEntriesByHandle(handle = handleType.handle)
            .mapCatching { insertAndReturnRootEntries(it).toDomain() }
            .toRepositoryResult()

    private suspend fun insertAndReturnRootEntries(entries: NavEntriesByHandleQuery.Data): List<NavigationEntryEntity> =
        navigationEntryDao.insert(entries = entries.toEntity())
            .filter { it.parentId == null }
}
