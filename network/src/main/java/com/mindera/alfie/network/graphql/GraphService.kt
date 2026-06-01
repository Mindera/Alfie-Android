package com.mindera.alfie.network.graphql

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Query

abstract class GraphService(private val apolloClient: ApolloClient) {

    protected fun <D : Query.Data> query(
        query: Query<D>
    ): ApolloCall<D> = apolloClient.query(query)

    protected fun <D : Mutation.Data> mutate(
        mutation: Mutation<D>
    ): ApolloCall<D> = apolloClient.mutation(mutation)
}
