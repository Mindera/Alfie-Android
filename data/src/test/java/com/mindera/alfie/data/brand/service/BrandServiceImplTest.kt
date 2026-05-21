package com.mindera.alfie.data.brand.service

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.mindera.alfie.core.test.setPrivatePropertyField
import com.mindera.alfie.data.brand.brandData
import com.mindera.alfie.graphql.BrandsQuery
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
internal class BrandServiceImplTest {

    @RelaxedMockK
    private lateinit var apolloCall: ApolloCall<BrandsQuery.Data>

    @RelaxedMockK
    private lateinit var apolloClient: ApolloClient

    @InjectMockKs
    private lateinit var subject: BrandServiceImpl

    @Test
    fun testGetBrands() = runTest {
        val expected = Result.success(brandData)

        val mockResponse = mockk<ApolloResponse<BrandsQuery.Data>>()
        mockResponse.setPrivatePropertyField("data", brandData)

        coEvery { apolloClient.query(BrandsQuery()) } returns apolloCall
        coEvery { apolloCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns false
        every { mockResponse.dataAssertNoErrors } returns brandData

        val result = subject.getBrands()

        assert(result.isSuccess)
        assertEquals(expected, result)
    }
}
