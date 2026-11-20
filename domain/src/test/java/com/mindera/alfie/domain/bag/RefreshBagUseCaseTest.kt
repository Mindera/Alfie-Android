package com.mindera.alfie.domain.bag

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.bag.RefreshBagUseCase
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
class RefreshBagUseCaseTest {
    @RelaxedMockK
    private lateinit var bagRepository: BagRepository

    @InjectMockKs
    lateinit var subject: RefreshBagUseCase

    @Test
    fun `invoke - WHEN the fetch succeeds THEN the fetched Bag is returned`() = runTest {
        val bag = mockk<Bag>()
        coEvery { bagRepository.refreshBag() } returns RepositoryResult.Success(bag)

        assertEquals(UseCaseResult.Success(bag), subject())
    }

    @Test
    fun `invoke - WHEN the fetch fails THEN the error is returned`() = runTest {
        val error = mockk<ErrorResult>()
        coEvery { bagRepository.refreshBag() } returns RepositoryResult.Error(error)

        assertEquals(UseCaseResult.Error(error), subject())
    }
}
