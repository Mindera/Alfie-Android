package com.mindera.alfie.domain.bag

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.bag.RemoveFromBagUseCase
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.bag.BagRepository
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.RepositoryResult
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
class RemoveFromBagUseCaseTest {
    @RelaxedMockK
    private lateinit var bagRepository: BagRepository

    @InjectMockKs
    lateinit var subject: RemoveFromBagUseCase

    @Test
    fun `invoke - WHEN the remove succeeds THEN the resulting Bag is returned`() = runTest {
        val bag = mockk<Bag>()
        coEvery { bagRepository.removeFromBag("line-1") } returns RepositoryResult.Success(bag)

        assertEquals(UseCaseResult.Success(bag), subject("line-1"))
    }

    @Test
    fun `invoke - WHEN the remove fails THEN the error is returned`() = runTest {
        val error = mockk<ErrorResult>()
        coEvery { bagRepository.removeFromBag("line-1") } returns RepositoryResult.Error(error)

        assertEquals(UseCaseResult.Error(error), subject("line-1"))
    }
}
