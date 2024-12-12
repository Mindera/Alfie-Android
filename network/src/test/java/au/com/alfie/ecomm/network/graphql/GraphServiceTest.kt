package au.com.alfie.ecomm.network.graphql

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class GraphServiceTest {

    @RelaxedMockK
    lateinit var apolloQuery: Query<Query.Data>

    @RelaxedMockK
    lateinit var apolloMutation: Mutation<Mutation.Data>

    @RelaxedMockK
    lateinit var apolloClient: ApolloClient

    private lateinit var subject: UnitTestGraphService

    @BeforeEach
    fun setup() {
        subject = UnitTestGraphService(apolloQuery, apolloMutation, apolloClient)
    }

    @Test
    fun query() = runTest {
        subject.query()

        verify { apolloClient.query(apolloQuery) }
    }

    @Test
    fun mutate() = runTest {
        subject.mutate()

        verify { apolloClient.mutation(apolloMutation) }
    }
}

internal class UnitTestGraphService(
    private val apolloQuery: Query<Query.Data>,
    private val apolloMutation: Mutation<Mutation.Data>,
    apolloClient: ApolloClient
) : GraphService(apolloClient) {

    fun query() {
        query(apolloQuery)
    }

    fun mutate() {
        mutate(apolloMutation)
    }
}
