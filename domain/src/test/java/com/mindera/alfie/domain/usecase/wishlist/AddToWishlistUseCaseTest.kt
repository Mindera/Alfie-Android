package com.mindera.alfie.domain.usecase.wishlist

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.ErrorType
import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.wishlist.WishlistRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class)
class AddToWishlistUseCaseTest {

    @RelaxedMockK
    private lateinit var wishlistRepository: WishlistRepository

    @InjectMockKs
    lateinit var subject: AddToWishlistUseCase

    @Test
    fun `add to wishlist - returns success`() = runTest {
        coEvery { wishlistRepository.addToWishlist("product-1") } returns RepositoryResult.Success(Unit)

        val result = subject("product-1")

        assertEquals(UseCaseResult.Success(Unit), result)
    }

    @Test
    fun `add to wishlist - returns error`() = runTest {
        val errorResult = ErrorResult(type = ErrorType.GENERIC_ERROR)
        coEvery { wishlistRepository.addToWishlist("product-1") } returns RepositoryResult.Error(errorResult)

        val result = subject("product-1")

        assertIs<UseCaseResult.Error>(result)
    }
}