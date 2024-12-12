package au.com.alfie.ecomm.data.search.repository

import au.com.alfie.ecomm.data.database.search.RecentSearchDao
import au.com.alfie.ecomm.data.search.mapper.toDomain
import au.com.alfie.ecomm.data.search.mapper.toEntity
import au.com.alfie.ecomm.data.search.service.SearchService
import au.com.alfie.ecomm.data.toRepositoryResult
import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.search.SearchRepository
import au.com.alfie.ecomm.repository.search.model.RecentSearch
import au.com.alfie.ecomm.repository.search.model.SearchSuggestions
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
