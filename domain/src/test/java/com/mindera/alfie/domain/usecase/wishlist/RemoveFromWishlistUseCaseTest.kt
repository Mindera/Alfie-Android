package com.mindera.alfie.domain.usecase.wishlist

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.product.ProductRepository
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.wishlist.WishlistRepository
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
class RemoveFromWishlistUseCaseTest {
    @RelaxedMockK
    private lateinit var wishlistRepository: WishlistRepository

    @RelaxedMockK
    private lateinit var productRepository: ProductRepository

    @InjectMockKs
    lateinit var subject: RemoveFromWishlistUseCase

    @Test
    fun `remove from wishlist, with success result`() = runTest {
        val mockProduct = mockk<Product>()
        coEvery { productRepository.getProduct(any()) } returns RepositoryResult.Success(mockProduct)
        coEvery { wishlistRepository.removeFromWishlist(mockProduct) } returns RepositoryResult.Success(true)

        val expected = UseCaseResult.Success(true)

        val result = subject("10")

        assertEquals(expected, result)
    }

    @Test
    fun `remove from wishlist, with error result`() = runTest {
        val mockProduct = mockk<Product>()
        val errorResult = mockk<ErrorResult>()
        coEvery { productRepository.getProduct(any()) } returns RepositoryResult.Error(errorResult)
        coEvery { wishlistRepository.removeFromWishlist(mockProduct) } returns RepositoryResult.Error(errorResult)

        val expected = UseCaseResult.Error(errorResult)

        val result = subject("10")

        assertEquals<Any>(expected, result)
    }
}