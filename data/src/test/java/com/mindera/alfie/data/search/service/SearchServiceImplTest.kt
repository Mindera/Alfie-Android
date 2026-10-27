package com.mindera.alfie.data.search.service

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.exception.DefaultApolloException
import com.mindera.alfie.core.test.setPrivatePropertyField
import com.mindera.alfie.graphql.bff.SearchSuggestionsQuery
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class SearchServiceImplTest {

    @RelaxedMockK
    private lateinit var apolloCall: ApolloCall<SearchSuggestionsQuery.Data>

    @RelaxedMockK
    private lateinit var apolloClient: ApolloClient

    @InjectMockKs
    private lateinit var service: SearchServiceImpl

    private val query = SearchSuggestionsQuery(query = "query", limit = Optional.present(6))

    @Test
    fun `getSearchSuggestions - WHEN response is success THEN returns success`() = runTest {
        val expectedData = SearchSuggestionsQuery.Data(mockk())
        val mockResponse = mockk<ApolloResponse<SearchSuggestionsQuery.Data>>()
        mockResponse.setPrivatePropertyField("data", expectedData)

        coEvery { apolloClient.query(query) } returns apolloCall
        coEvery { apolloCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns false
        every { mockResponse.dataAssertNoErrors } returns expectedData

        val result = service.getSearchSuggestions(query = "query", limit = 6)

        assertEquals(Result.success(expectedData), result)
    }

    @Test
    fun `getSearchSuggestions - WHEN response has errors THEN returns failure`() = runTest {
        val mockResponse = mockk<ApolloResponse<SearchSuggestionsQuery.Data>>()
        mockResponse.setPrivatePropertyField("exception", DefaultApolloException("Error message"))

        coEvery { apolloClient.query(query) } returns apolloCall
        coEvery { apolloCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns true

        val result = service.getSearchSuggestions(query = "query", limit = 6)

        assertTrue(result.isFailure)
    }
}
