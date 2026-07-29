package com.mindera.alfie.data.navigation.remote.service

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.mindera.alfie.core.test.setPrivatePropertyField
import com.mindera.alfie.graphql.bff.MainMenuQuery
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

@ExtendWith(MockKExtension::class)
internal class RemoteNavigationServiceImplTest {

    @RelaxedMockK
    private lateinit var apolloCall: ApolloCall<MainMenuQuery.Data>

    @RelaxedMockK
    private lateinit var apolloClient: ApolloClient

    @InjectMockKs
    private lateinit var subject: RemoteNavigationServiceImpl

    @Test
    fun testGetNavEntriesByHandle() = runTest {
        val expectedData = MainMenuQuery.Data(mockk())
        val expectedResult = Result.success(expectedData)
        val navEntriesQuery = MainMenuQuery("handle")
        val mockResponse = mockk<ApolloResponse<MainMenuQuery.Data>>()
        mockResponse.setPrivatePropertyField("data", expectedData)

        coEvery { apolloClient.query(navEntriesQuery) } returns apolloCall
        coEvery { apolloCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns false
        every { mockResponse.dataAssertNoErrors } returns expectedData

        val result = subject.getNavEntriesByHandle("handle")

        assert(result.isSuccess)
        assertEquals(expectedResult, result)
    }
}
