package com.mindera.alfie.data.search.service

import com.mindera.alfie.graphql.SearchSuggestionsQuery

internal interface SearchService {

    suspend fun getSearchSuggestions(query: String): Result<SearchSuggestionsQuery.Data>
}
