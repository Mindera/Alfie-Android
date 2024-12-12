package au.com.alfie.ecomm.domain.usecase.search

import au.com.alfie.ecomm.repository.search.SearchRepository
import javax.inject.Inject

class ClearRecentSearchesUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    suspend operator fun invoke() = repository.clearRecentSearches()
}
