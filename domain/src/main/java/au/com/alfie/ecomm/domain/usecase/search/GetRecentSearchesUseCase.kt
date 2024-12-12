package au.com.alfie.ecomm.domain.usecase.search

import au.com.alfie.ecomm.repository.search.SearchRepository
import au.com.alfie.ecomm.repository.search.model.RecentSearch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchesUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    operator fun invoke(): Flow<List<RecentSearch>> = repository.getRecentSearches()
}
