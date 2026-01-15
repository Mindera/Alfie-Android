package au.com.alfie.ecomm.domain.usecase.wishlist

import app.cash.turbine.test
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.wishlist.WishlistRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class GetWishlistUseCaseTest {
    @RelaxedMockK
    private lateinit var wishlistRepository: WishlistRepository

    @InjectMockKs
    lateinit var subject: GetWishlistUseCase

    @Test
    fun `get list of products in the wishlist`() = runTest {
        val mockWishlist = mockk<List<Product>>()
        coEvery { wishlistRepository.getWishlist() } returns flowOf(RepositoryResult.Success(mockWishlist))

        val expected = UseCaseResult.Success(mockWishlist)

        subject().test {
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `get list of products in the wishlist, and returns an error`() = runTest {
        val errorResult = mockk<ErrorResult>()
        coEvery { wishlistRepository.getWishlist() } returns flowOf(RepositoryResult.Error(errorResult))

        val expected = UseCaseResult.Error(errorResult)

        subject().test {
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}