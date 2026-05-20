package com.mindera.alfie.repository.search

import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.search.model.RecentSearch
import com.mindera.alfie.repository.search.model.SearchSuggestions
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getRecentSearches(): Flow<List<RecentSearch>>

    suspend fun saveRecentSearch(recentSearch: RecentSearch)

    suspend fun deleteRecentSearch(recentSearch: RecentSearch)

    suspend fun clearRecentSearches()

    suspend fun getSearchSuggestions(query: String): RepositoryResult<SearchSuggestions>
}
