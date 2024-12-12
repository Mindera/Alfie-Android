package au.com.alfie.ecomm.data.search.service

import au.com.alfie.ecomm.graphql.SearchSuggestionsQuery
import au.com.alfie.ecomm.network.extension.unwrap
import au.com.alfie.ecomm.network.graphql.GraphService
import com.apollographql.apollo3.ApolloClient
import javax.inject.Inject

internal class SearchServiceImpl @Inject constructor(
    apolloClient: ApolloClient
) : GraphService(apolloClient), SearchService {

    override suspend fun getSearchSuggestions(query: String): Result<SearchSuggestionsQuery.Data> =
        query(SearchSuggestionsQuery(query = query)).unwrap()
}
