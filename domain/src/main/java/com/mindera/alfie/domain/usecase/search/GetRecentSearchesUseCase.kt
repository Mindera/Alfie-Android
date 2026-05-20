package com.mindera.alfie.domain.usecase.search

import com.mindera.alfie.repository.search.SearchRepository
import com.mindera.alfie.repository.search.model.RecentSearch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchesUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    operator fun invoke(): Flow<List<RecentSearch>> = repository.getRecentSearches()
}
