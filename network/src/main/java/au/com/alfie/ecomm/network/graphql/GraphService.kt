package au.com.alfie.ecomm.network.graphql

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query

abstract class GraphService(private val apolloClient: ApolloClient) {

    protected fun <D : Query.Data> query(
        query: Query<D>
    ): ApolloCall<D> = apolloClient.query(query)

    protected fun <D : Mutation.Data> mutate(
        mutation: Mutation<D>
    ): ApolloCall<D> = apolloClient.mutation(mutation)
}
