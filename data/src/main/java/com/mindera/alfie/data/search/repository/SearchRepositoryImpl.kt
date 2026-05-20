package com.mindera.alfie.data.search.repository

import com.mindera.alfie.data.database.search.RecentSearchDao
import com.mindera.alfie.data.search.mapper.toDomain
import com.mindera.alfie.data.search.mapper.toEntity
import com.mindera.alfie.data.search.service.SearchService
import com.mindera.alfie.data.toRepositoryResult
import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.search.SearchRepository
import com.mindera.alfie.repository.search.model.RecentSearch
import com.mindera.alfie.repository.search.model.SearchSuggestions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val searchService: SearchService,
    private val recentSearchDao: RecentSearchDao
) : SearchRepository {

    override fun getRecentSearches(): Flow<List<RecentSearch>> = recentSearchDao
        .getRecentSearches()
        .map { it.toDomain() }

    override suspend fun saveRecentSearch(recentSearch: RecentSearch) {
        recentSearchDao.saveRecentSearch(recentSearch.toEntity())
        recentSearchDao.clearOldSearches()
    }

    override suspend fun deleteRecentSearch(recentSearch: RecentSearch) {
        recentSearchDao.deleteRecentSearch(recentSearch.toEntity())
    }

    override suspend fun clearRecentSearches() {
        recentSearchDao.clearRecentSearches()
    }

    override suspend fun getSearchSuggestions(query: String): RepositoryResult<SearchSuggestions> =
        searchService.getSearchSuggestions(query = query)
            .mapCatching { it.suggestion.toDomain() }
            .toRepositoryResult()
}
