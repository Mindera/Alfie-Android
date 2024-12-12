package au.com.alfie.ecomm.data.productlist.service

import au.com.alfie.ecomm.core.test.setPrivatePropertyField
import au.com.alfie.ecomm.graphql.ProductListingQuery
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

@ExtendWith(MockKExtension::class)
class ProductListServiceImplTest {

    @RelaxedMockK
    private lateinit var apolloCall: ApolloCall<ProductListingQuery.Data>

    @RelaxedMockK
    private lateinit var apolloClient: ApolloClient

    @InjectMockKs
    private lateinit var service: ProductListServiceImpl

    @Test
    fun testGetProductListSuccess() = runTest {
        val expectedData = ProductListingQuery.Data(mockk())
        val expectedResult = Result.success(expectedData)
        val productListingQuery = ProductListingQuery(
            offset = 0,
            limit = 15
        )
        val mockResponse = mockk<ApolloResponse<ProductListingQuery.Data>>()
        mockResponse.setPrivatePropertyField("data", expectedData)

        coEvery { apolloClient.query(productListingQuery) } returns apolloCall
        coEvery { apolloCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns false
        every { mockResponse.dataAssertNoErrors } returns expectedData

        val result = service.getProductList(
            offset = 0,
            limit = 15,
            categoryId = null,
            query = null
        )

        assert(result.isSuccess)
        assertEquals(expectedResult, result)
    }

    @Test
    fun testGetProductListError() = runTest {
        val error = DefaultApolloException("Error message")
        val productListingQuery = ProductListingQuery(
            offset = 0,
            limit = 15
        )
        val mockResponse = mockk<ApolloResponse<ProductListingQuery.Data>>()
        mockResponse.setPrivatePropertyField("exception", error)

        coEvery { apolloClient.query(productListingQuery) } returns apolloCall
        coEvery { apolloCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns true

        val result = service.getProductList(
            offset = 0,
            limit = 15,
            categoryId = null,
            query = null
        )

        assert(result.isFailure)
    }
}
