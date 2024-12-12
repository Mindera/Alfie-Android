package au.com.alfie.ecomm.domain.usecase.search

import au.com.alfie.ecomm.repository.search.SearchRepository
import au.com.alfie.ecomm.repository.search.model.RecentSearch
import javax.inject.Inject

class SaveRecentSearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    suspend operator fun invoke(recentSearch: RecentSearch) = repository.saveRecentSearch(recentSearch)
}
