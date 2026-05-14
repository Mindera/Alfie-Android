package com.mindera.alfie.domain.bag

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.bag.GetBagUseCase
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.bag.BagRepository
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class GetBagUseCaseTest {
    @RelaxedMockK
    private lateinit var bagRepository: BagRepository

    @InjectMockKs
    lateinit var subject: GetBagUseCase

    @Test
    fun `get list of products in the bag`() = runTest {
        val mockBag = mockk<List<BagProduct>>()
        coEvery { bagRepository.getBag() } returns flowOf(RepositoryResult.Success(mockBag))

        val expected = UseCaseResult.Success(mockBag)
        val result = subject().first()

        assertEquals(expected, result)
    }

    @Test
    fun `get list of products in the bag, and returns an error`() = runTest {
        val errorResult = mockk<ErrorResult>()
        coEvery { bagRepository.getBag() } returns flowOf(RepositoryResult.Error(errorResult))

        val expected = UseCaseResult.Error(errorResult)
        val result = subject().first()

        assertEquals(expected, result)
    }
}