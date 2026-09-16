package com.mindera.alfie.domain.bag

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.bag.RemoveAllFromBagUseCase
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
class RemoveAllFromBagUseCaseTest {

    @RelaxedMockK
    private lateinit var bagRepository: BagRepository

    @InjectMockKs
    lateinit var subject: RemoveAllFromBagUseCase

    private val bagProduct = BagProduct(productId = "linen-shirt", variantSku = "0283/764")

    @Test
    fun `invoke - WHEN the repository succeeds THEN the success is forwarded`() = runTest {
        coEvery { bagRepository.removeAllFromBag(bagProduct) } returns RepositoryResult.Success(true)

        val result = subject(bagProduct)

        coVerify(exactly = 1) { bagRepository.removeAllFromBag(bagProduct) }
        assertIs<UseCaseResult.Success<Boolean>>(result)
        assertTrue(result.data)
    }

    @Test
    fun `invoke - WHEN the repository errors THEN the error is forwarded`() = runTest {
        val errorResult = mockk<ErrorResult>()
        coEvery { bagRepository.removeAllFromBag(bagProduct) } returns RepositoryResult.Error(errorResult)

        val result = subject(bagProduct)

        coVerify(exactly = 1) { bagRepository.removeAllFromBag(bagProduct) }
        assertIs<UseCaseResult.Error>(result)
        assertEquals(errorResult, result.error)
    }

    @Test
    fun `invoke - removes every unit rather than a single one`() = runTest {
        coEvery { bagRepository.removeAllFromBag(bagProduct) } returns RepositoryResult.Success(true)

        subject(bagProduct)

        coVerify(exactly = 0) { bagRepository.removeFromBag(any()) }
    }
}
