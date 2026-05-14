package com.mindera.alfie.domain.usecase.search

import com.mindera.alfie.repository.search.SearchRepository
import javax.inject.Inject

class ClearRecentSearchesUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    suspend operator fun invoke() = repository.clearRecentSearches()
}
