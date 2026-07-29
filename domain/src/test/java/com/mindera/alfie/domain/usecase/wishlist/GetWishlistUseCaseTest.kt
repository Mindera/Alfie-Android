package com.mindera.alfie.domain.usecase.wishlist

import app.cash.turbine.test
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.core.test.TestDispatcherProvider
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.product.ProductRepository
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.ErrorType
import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.wishlist.WishlistRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class GetWishlistUseCaseTest {

    @RelaxedMockK
    private lateinit var wishlistRepository: WishlistRepository

    @RelaxedMockK
    private lateinit var productRepository: ProductRepository

    private lateinit var subject: GetWishlistUseCase

    @BeforeEach
    fun setUp() {
        subject = GetWishlistUseCase(
            wishlistRepository = wishlistRepository,
            productRepository = productRepository,
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    @Test
    fun `invoke - returns success with list of products when all fetches succeed`() = runTest {
        val mockProduct1 = mockk<Product>()
        val mockProduct2 = mockk<Product>()
        coEvery { wishlistRepository.getWishlist() } returns flowOf(listOf("id1", "id2"))
        coEvery { productRepository.getProduct("id1") } returns RepositoryResult.Success(mockProduct1)
        coEvery { productRepository.getProduct("id2") } returns RepositoryResult.Success(mockProduct2)

        val expected = UseCaseResult.Success(listOf(mockProduct1, mockProduct2))

        subject().test {
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke - returns partial success when some product fetches fail`() = runTest {
        val mockProduct1 = mockk<Product>()
        coEvery { wishlistRepository.getWishlist() } returns flowOf(listOf("id1", "id2"))
        coEvery { productRepository.getProduct("id1") } returns RepositoryResult.Success(mockProduct1)
        coEvery { productRepository.getProduct("id2") } returns RepositoryResult.Error(ErrorResult(type = ErrorType.GENERIC_ERROR))

        val expected = UseCaseResult.Success(listOf(mockProduct1))

        subject().test {
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke - returns empty success list when wishlist is empty`() = runTest {
        coEvery { wishlistRepository.getWishlist() } returns flowOf(emptyList())

        subject().test {
            assertEquals(UseCaseResult.Success(emptyList()), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}