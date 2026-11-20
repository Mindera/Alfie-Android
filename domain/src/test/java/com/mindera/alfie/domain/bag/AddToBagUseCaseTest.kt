package com.mindera.alfie.domain.bag

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.bag.AddToBagUseCase
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
class AddToBagUseCaseTest {
    @RelaxedMockK
    private lateinit var bagRepository: BagRepository

    @InjectMockKs
    lateinit var subject: AddToBagUseCase

    @Test
    fun `invoke - WHEN the add succeeds THEN the resulting Bag is returned`() = runTest {
        val bag = mockk<Bag>()
        coEvery { bagRepository.addToBag(variantId = "v1", productId = "p1", quantity = 2) } returns RepositoryResult.Success(bag)

        val result = subject(variantId = "v1", productId = "p1", quantity = 2)

        assertEquals(UseCaseResult.Success(bag), result)
    }

    @Test
    fun `invoke - WHEN the add fails THEN the error is returned`() = runTest {
        val error = mockk<ErrorResult>()
        coEvery { bagRepository.addToBag(variantId = "v1", productId = null, quantity = 1) } returns RepositoryResult.Error(error)

        val result = subject(variantId = "v1", productId = null)

        assertEquals(UseCaseResult.Error(error), result)
    }
}
