package au.com.alfie.ecomm.repository.navigation

import au.com.alfie.ecomm.repository.navigation.model.HandleType
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import au.com.alfie.ecomm.repository.result.RepositoryResult

interface NavigationRepository {

    suspend fun getByParentId(parentId: Int): List<NavEntry>

    suspend fun getNavEntriesByHandle(handleType: HandleType): RepositoryResult<List<NavEntry>>
}
