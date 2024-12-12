package au.com.alfie.ecomm.data.product.service

import au.com.alfie.ecomm.core.test.setPrivatePropertyField
import au.com.alfie.ecomm.data.product.productData
import au.com.alfie.ecomm.graphql.ProductQuery
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
internal class ProductServiceImplTest {

    @RelaxedMockK
    private lateinit var apolloCall: ApolloCall<ProductQuery.Data>

    @RelaxedMockK
    private lateinit var apolloClient: ApolloClient

    @InjectMockKs
    private lateinit var subject: ProductServiceImpl

    @Test
    fun testGetProduct() = runTest {
        val productId = "2666503"
        val expectedResult = Result.success(productData)
        val productQuery = ProductQuery(productId)
        val mockResponse = mockk<ApolloResponse<ProductQuery.Data>>()
        mockResponse.setPrivatePropertyField("data", productData)

        coEvery { apolloClient.query(productQuery) } returns apolloCall
        coEvery { apolloCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns false
        every { mockResponse.dataAssertNoErrors } returns productData

        val result = subject.getProduct(productId)

        assert(result.isSuccess)
        assertEquals(expectedResult, result)
    }
}
