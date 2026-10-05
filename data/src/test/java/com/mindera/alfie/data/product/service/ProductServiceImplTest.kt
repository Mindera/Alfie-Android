package com.mindera.alfie.data.product.service

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.exception.DefaultApolloException
import com.mindera.alfie.core.test.setPrivatePropertyField
import com.mindera.alfie.graphql.bff.GetProductDetailsQuery
import com.mindera.alfie.graphql.bff.ProductByBarcodeQuery
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
    private lateinit var apolloCall: ApolloCall<GetProductDetailsQuery.Data>

    @RelaxedMockK
    private lateinit var barcodeCall: ApolloCall<ProductByBarcodeQuery.Data>

    @RelaxedMockK
    private lateinit var apolloClient: ApolloClient

    @InjectMockKs
    private lateinit var service: ProductServiceImpl

    @Test
    fun `getProduct - WHEN response is success THEN returns success`() = runTest {
        val expectedData = mockk<GetProductDetailsQuery.Data>()
        val expectedResult = Result.success(expectedData)
        val mockResponse = mockk<ApolloResponse<GetProductDetailsQuery.Data>>()
        mockResponse.setPrivatePropertyField("data", expectedData)

        coEvery { apolloClient.query(any<GetProductDetailsQuery>()) } returns apolloCall
        coEvery { apolloCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns false
        every { mockResponse.dataAssertNoErrors } returns expectedData

        val result = service.getProduct(handle = "test-handle")

        assert(result.isSuccess)
        assertEquals(expectedResult, result)
    }

    @Test
    fun `getProduct - WHEN response has errors THEN returns failure`() = runTest {
        val error = DefaultApolloException("Error message")
        val mockResponse = mockk<ApolloResponse<GetProductDetailsQuery.Data>>()
        mockResponse.setPrivatePropertyField("exception", error)

        coEvery { apolloClient.query(any<GetProductDetailsQuery>()) } returns apolloCall
        coEvery { apolloCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns true

        val result = service.getProduct(handle = "test-handle")

        assert(result.isFailure)
    }

    @Test
    fun `getProductByBarcode - WHEN response is success THEN returns success`() = runTest {
        val expectedData = mockk<ProductByBarcodeQuery.Data>()
        val mockResponse = mockk<ApolloResponse<ProductByBarcodeQuery.Data>>()
        mockResponse.setPrivatePropertyField("data", expectedData)

        coEvery { apolloClient.query(any<ProductByBarcodeQuery>()) } returns barcodeCall
        coEvery { barcodeCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns false
        every { mockResponse.dataAssertNoErrors } returns expectedData

        val result = service.getProductByBarcode(barcode = "5012345678900")

        assert(result.isSuccess)
        assertEquals(Result.success(expectedData), result)
    }

    @Test
    fun `getProductByBarcode - WHEN response has errors THEN returns failure`() = runTest {
        val error = DefaultApolloException("Error message")
        val mockResponse = mockk<ApolloResponse<ProductByBarcodeQuery.Data>>()
        mockResponse.setPrivatePropertyField("exception", error)

        coEvery { apolloClient.query(any<ProductByBarcodeQuery>()) } returns barcodeCall
        coEvery { barcodeCall.execute() } returns mockResponse
        every { mockResponse.hasErrors() } returns true

        val result = service.getProductByBarcode(barcode = "5012345678900")

        assert(result.isFailure)
    }
}
