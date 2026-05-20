package com.mindera.alfie.domain.bag

import com.mindera.alfie.domain.usecase.bag.AddToBagUseCase
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.bag.BagRepository
import com.mindera.alfie.repository.product.ProductRepository
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

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
        val mockProduct = mockk<BagProduct> {
            every { productId } returns "10"
            every { variantSku } returns "34535"
        }

        coEvery { bagRepository.addToBag(mockProduct) } returns RepositoryResult.Success(true)
        subject(productId = "10", variantSku = "34535")
        coVerify { bagRepository.addToBag(mockProduct) }
    }

    @Test
    fun `add to bag, with error result`() = runTest {
        val mockProduct = mockk<BagProduct> {
            every { productId } returns "10"
            every { variantSku } returns "34535"
        }
        val errorResult = mockk<ErrorResult>()

        coEvery { bagRepository.addToBag(mockProduct) } returns RepositoryResult.Error(errorResult)
        subject("10", "34535")
        coVerify { bagRepository.addToBag(mockProduct) }
    }
}