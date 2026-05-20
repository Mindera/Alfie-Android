package com.mindera.alfie.domain.usecase.search

import com.mindera.alfie.repository.search.SearchRepository
import com.mindera.alfie.repository.search.model.RecentSearch
import javax.inject.Inject

class DeleteRecentSearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    suspend operator fun invoke(recentSearch: RecentSearch) = repository.deleteRecentSearch(recentSearch)
}
