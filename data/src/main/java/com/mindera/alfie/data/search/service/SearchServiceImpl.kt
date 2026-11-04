package com.mindera.alfie.data.search.service

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.mindera.alfie.graphql.bff.SearchSuggestionsQuery
import com.mindera.alfie.network.extension.unwrap
import com.mindera.alfie.network.graphql.GraphService
import javax.inject.Inject

internal class SearchServiceImpl @Inject constructor(
    apolloClient: ApolloClient
) : GraphService(apolloClient), SearchService {

    override suspend fun getSearchSuggestions(
        query: String,
        limit: Int
    ): Result<SearchSuggestionsQuery.Data> =
        query(
            SearchSuggestionsQuery(
                query = query,
                limit = Optional.present(limit)
            )
        ).unwrap()
}
