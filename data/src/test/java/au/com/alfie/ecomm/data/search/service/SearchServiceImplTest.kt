package au.com.alfie.ecomm.data.search.service

import au.com.alfie.ecomm.core.test.setPrivatePropertyField
import au.com.alfie.ecomm.graphql.SearchSuggestionsQuery
import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.DefaultApolloException
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

    @Test
    fun testGetSearchSuggestionsSuccess() = runTest {
        val expectedData = SearchSuggestionsQuery.Data(mockk())
        val expectedResult = Result.success(expectedData)
        val query = SearchSuggestionsQuery(query = "query")
        val mockResponse = mockk<ApolloResponse<SearchSuggestionsQuery.Data>>()
        mockResponse.setPrivatePropertyField("data", expectedData)

        coEvery { apolloClient.query(query) } returns apolloCall
        coEvery { apolloCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns false
        every { mockResponse.dataAssertNoErrors } returns expectedData

        val result = service.getSearchSuggestions(query = "query")

        assertTrue(result.isSuccess)
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetSearchSuggestionError() = runTest {
        val error = DefaultApolloException("Error message")
        val query = SearchSuggestionsQuery(query = "query")
        val mockResponse = mockk<ApolloResponse<SearchSuggestionsQuery.Data>>()
        mockResponse.setPrivatePropertyField("exception", error)

        coEvery { apolloClient.query(query) } returns apolloCall
        coEvery { apolloCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns true

        val result = service.getSearchSuggestions(query = "query")

        assertTrue(result.isFailure)
    }
}
