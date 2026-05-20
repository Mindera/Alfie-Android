package com.mindera.alfie.data.product.repository

import com.mindera.alfie.data.product.product
import com.mindera.alfie.data.product.productData
import com.mindera.alfie.data.product.service.ProductService
import com.mindera.alfie.graphql.ProductQuery
import com.mindera.alfie.network.exception.GraphNetworkException
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.ErrorType
import com.mindera.alfie.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class ProductRepositoryImplTest {

    companion object {
        private const val PRODUCT_ID = "2666503"
    }

    @RelaxedMockK
    lateinit var productService: ProductService

    @InjectMockKs
    lateinit var subject: ProductRepositoryImpl

    @Test
    fun `GIVEN getProduct WHEN result is success THEN is mapped to repository result`() = runTest {
        val expected = RepositoryResult.Success(product)
        coEvery { productService.getProduct(any()) } returns Result.success(productData)

        val result = subject.getProduct(productId = PRODUCT_ID)

        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN getProduct WHEN result is failure THEN exception is wrapped in error result`() = runTest {
        val expected = RepositoryResult.Error(
            ErrorResult(
                type = ErrorType.UNKNOWN,
                errorMessage = "Error",
                code = "0"
            )
        )
        coEvery { productService.getProduct(any()) } returns Result.failure(
            GraphNetworkException.UnexpectedException(message = "Error")
        )

        val result = subject.getProduct(productId = PRODUCT_ID)

        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN getProduct WHEN result is success but null THEN exception is wrapped in error result`() = runTest {
        val expectedServiceResponse = ProductQuery.Data(product = null)
        val expected = RepositoryResult.Error(
            ErrorResult(
                type = ErrorType.UNKNOWN,
                errorMessage = null,
                code = null
            )
        )
        coEvery { productService.getProduct(any()) } returns Result.success(expectedServiceResponse)

        val result = subject.getProduct(productId = PRODUCT_ID)

        assertEquals(expected, result)
    }
}
