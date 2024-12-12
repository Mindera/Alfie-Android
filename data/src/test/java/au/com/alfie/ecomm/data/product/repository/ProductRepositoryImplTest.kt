package au.com.alfie.ecomm.data.product.repository

import au.com.alfie.ecomm.data.product.product
import au.com.alfie.ecomm.data.product.productData
import au.com.alfie.ecomm.data.product.service.ProductService
import au.com.alfie.ecomm.graphql.ProductQuery
import au.com.alfie.ecomm.network.exception.GraphNetworkException
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.ErrorType
import au.com.alfie.ecomm.repository.result.RepositoryResult
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
