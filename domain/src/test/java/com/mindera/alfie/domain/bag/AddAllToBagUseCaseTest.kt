package com.mindera.alfie.domain.bag

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.bag.AddAllToBagUseCase
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.bag.BagRepository
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class AddAllToBagUseCaseTest {

    @RelaxedMockK
    private lateinit var bagRepository: BagRepository

    @InjectMockKs
    lateinit var subject: AddAllToBagUseCase

    private val bagProduct = BagProduct(productId = "linen-shirt", variantSku = "0283/764")

    @Test
    fun `invoke - WHEN the repository succeeds THEN the success is forwarded`() = runTest {
        coEvery { bagRepository.addAllToBag(bagProduct, 2, 0) } returns RepositoryResult.Success(true)

        val result = subject(bagProduct, quantity = 2, index = 0)

        coVerify(exactly = 1) { bagRepository.addAllToBag(bagProduct, 2, 0) }
        assertIs<UseCaseResult.Success<Boolean>>(result)
        assertTrue(result.data)
    }

    @Test
    fun `invoke - WHEN the repository errors THEN the error is forwarded`() = runTest {
        val errorResult = mockk<ErrorResult>()
        coEvery { bagRepository.addAllToBag(bagProduct, 2, 0) } returns RepositoryResult.Error(errorResult)

        val result = subject(bagProduct, quantity = 2, index = 0)

        coVerify(exactly = 1) { bagRepository.addAllToBag(bagProduct, 2, 0) }
        assertIs<UseCaseResult.Error>(result)
        assertEquals(errorResult, result.error)
    }
}
