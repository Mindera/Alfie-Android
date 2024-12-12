package au.com.alfie.ecomm.data.brand.service

import au.com.alfie.ecomm.core.test.setPrivatePropertyField
import au.com.alfie.ecomm.data.brand.brandData
import au.com.alfie.ecomm.graphql.BrandsQuery
import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
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
