package au.com.alfie.ecomm.domain.usecase.search

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.repository.search.SearchRepository
import javax.inject.Inject

class GetSearchSuggestionsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) : UseCaseInteractor {

    suspend operator fun invoke(query: String) = run(searchRepository.getSearchSuggestions(query = query))
}
