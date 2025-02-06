package au.com.alfie.ecomm.domain.bag

import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.domain.usecase.bag.AddToBagUseCase
import au.com.alfie.ecomm.repository.bag.BagRepository
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
class AddToBagUseCaseTest {
    @RelaxedMockK
    private lateinit var bagRepository: BagRepository

    @RelaxedMockK
    private lateinit var productRepository: ProductRepository

    @InjectMockKs
    lateinit var subject: AddToBagUseCase

    @Test
    fun `add to bag, with success result`() = runTest {
        val mockProduct = mockk<Product>()
        coEvery { productRepository.getProduct(any()) } returns RepositoryResult.Success(mockProduct)
        coEvery { bagRepository.addToBag(mockProduct) } returns RepositoryResult.Success(true)

        val expected = UseCaseResult.Success(true)

        val result = subject("10")

        assertEquals(expected, result)
    }

    @Test
    fun `add to bag, with error result`() = runTest {
        val mockProduct = mockk<Product>()
        val errorResult = mockk<ErrorResult>()
        coEvery { productRepository.getProduct(any()) } returns RepositoryResult.Error(errorResult)
        coEvery { bagRepository.addToBag(mockProduct) } returns RepositoryResult.Error(errorResult)

        val expected = UseCaseResult.Error(errorResult)

        val result = subject("10")

        assertEquals<Any>(expected, result)
    }
}