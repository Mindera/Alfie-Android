package au.com.alfie.ecomm.repository.search

import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.search.model.RecentSearch
import au.com.alfie.ecomm.repository.search.model.SearchSuggestions
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getRecentSearches(): Flow<List<RecentSearch>>

    suspend fun saveRecentSearch(recentSearch: RecentSearch)

    suspend fun deleteRecentSearch(recentSearch: RecentSearch)

    suspend fun clearRecentSearches()

    suspend fun getSearchSuggestions(query: String): RepositoryResult<SearchSuggestions>
}
