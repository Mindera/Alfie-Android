package com.mindera.alfie.domain.usecase.search

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.repository.search.SearchRepository
import javax.inject.Inject

class GetSearchSuggestionsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) : UseCaseInteractor {

    suspend operator fun invoke(query: String) = run(searchRepository.getSearchSuggestions(query = query))
}
