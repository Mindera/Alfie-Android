package au.com.alfie.ecomm.domain.usecase.product

import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.product.ProductRepository
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class GetProductUseCaseTest {

    @RelaxedMockK
    lateinit var productRepository: ProductRepository

    @InjectMockKs
    lateinit var subject: GetProductUseCase

    @Test
    fun `invoke - WHEN repository result is success THEN result is success`() = runTest {
        val mockProduct = mockk<Product>()

        coEvery {
            productRepository.getProduct(productId = "id")
        } returns RepositoryResult.Success(mockProduct)

        val expected = UseCaseResult.Success(mockProduct)

        val result = subject(productId = "id")

        assertEquals(expected, result)
    }

    @Test
    fun `invoke - WHEN repository result is error THEN result is error`() = runTest {
        val mockError = mockk<ErrorResult>()

        coEvery {
            productRepository.getProduct(productId = "id")
        } returns RepositoryResult.Error(mockError)

        val expected = UseCaseResult.Error(mockError)

        val result = subject(productId = "id")

        assertEquals(expected, result)
    }
}
