package com.mindera.alfie.data.search.service

import com.mindera.alfie.graphql.bff.SearchSuggestionsQuery

internal interface SearchService {

    suspend fun getSearchSuggestions(
        query: String,
        limit: Int
    ): Result<SearchSuggestionsQuery.Data>
}
