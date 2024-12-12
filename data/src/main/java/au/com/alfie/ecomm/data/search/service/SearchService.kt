package au.com.alfie.ecomm.data.search.service

import au.com.alfie.ecomm.graphql.SearchSuggestionsQuery

internal interface SearchService {

    suspend fun getSearchSuggestions(query: String): Result<SearchSuggestionsQuery.Data>
}
