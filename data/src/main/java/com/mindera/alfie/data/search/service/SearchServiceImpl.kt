package com.mindera.alfie.data.search.service

import com.apollographql.apollo.ApolloClient
import com.mindera.alfie.graphql.SearchSuggestionsQuery
import com.mindera.alfie.network.extension.unwrap
import com.mindera.alfie.network.graphql.GraphService
import javax.inject.Inject

internal class SearchServiceImpl @Inject constructor(
    apolloClient: ApolloClient
) : GraphService(apolloClient), SearchService {

    override suspend fun getSearchSuggestions(query: String): Result<SearchSuggestionsQuery.Data> =
        query(SearchSuggestionsQuery(query = query)).unwrap()
}
