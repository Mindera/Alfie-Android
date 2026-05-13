package au.com.alfie.ecomm.domain.usecase.wishlist

import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.ErrorType
import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.wishlist.WishlistRepository
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