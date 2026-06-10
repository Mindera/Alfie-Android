package com.mindera.alfie.repository.search

import com.mindera.alfie.repository.search.model.RecentSearch
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getRecentSearches(): Flow<List<RecentSearch>>

    suspend fun saveRecentSearch(recentSearch: RecentSearch)

    suspend fun deleteRecentSearch(recentSearch: RecentSearch)

    suspend fun clearRecentSearches()
}
