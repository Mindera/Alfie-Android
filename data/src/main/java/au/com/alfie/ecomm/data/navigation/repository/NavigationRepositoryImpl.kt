package au.com.alfie.ecomm.data.navigation.repository

import au.com.alfie.ecomm.data.database.navigation.NavigationEntryDao
import au.com.alfie.ecomm.data.database.navigation.model.NavigationEntryEntity
import au.com.alfie.ecomm.data.navigation.cache.mapper.toDomain
import au.com.alfie.ecomm.data.navigation.remote.mapper.toEntity
import au.com.alfie.ecomm.data.navigation.remote.service.RemoteNavigationService
import au.com.alfie.ecomm.data.toRepositoryResult
import au.com.alfie.ecomm.graphql.NavEntriesByHandleQuery
import au.com.alfie.ecomm.repository.navigation.NavigationRepository
import au.com.alfie.ecomm.repository.navigation.model.HandleType
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import au.com.alfie.ecomm.repository.result.RepositoryResult
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
