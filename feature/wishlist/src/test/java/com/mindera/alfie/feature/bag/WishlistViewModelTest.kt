package com.mindera.alfie.feature.bag

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
import com.mindera.alfie.feature.wishlist.WishlistUIFactory
import com.mindera.alfie.feature.wishlist.WishlistUiState
import com.mindera.alfie.feature.wishlist.WishlistViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class WishlistViewModelTest {
    @RelaxedMockK
    private lateinit var getWishlistUseCase: GetWishlistUseCase

    @RelaxedMockK
    private lateinit var removeFromWishlistUseCase: RemoveFromWishlistUseCase

    @RelaxedMockK
    private lateinit var wishlistUiFactory: WishlistUIFactory

    @Test
    fun `WHEN getWishlistList returns a success THEN update the state with the correct wishlist items`() = runTest {
        val wishlistProducts = products
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

        coEvery { getWishlistUseCase() } returns flow {
            emit(UseCaseResult.Success(wishlistProducts))
        }

        every { wishlistUiFactory(products, any()) } returns wishListProductUi
        every { savedStateHandle.get<Boolean>("launchFromTop") } returns false

        val viewModel = buildViewModel(savedStateHandle)

        viewModel.state.test {
            val result = awaitItem()
            assertEquals(WishlistUiState.Data.Loaded(wishListProductUi), result)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN getWishlistList returns an error THEN update the state with the error state`() = runTest {
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

        coEvery { getWishlistUseCase() } returns flow {
            emit(UseCaseResult.Error(mockk()))
        }

        every { savedStateHandle.get<Boolean>("launchFromTop") } returns false

        val viewModel = buildViewModel(savedStateHandle)

        viewModel.state.test {
            val result = awaitItem()
            assertEquals(WishlistUiState.Error, result)
            cancelAndConsumeRemainingEvents()
        }
    }

    private fun buildViewModel(savedStateHandle: SavedStateHandle) = WishlistViewModel(
        getWishlistUseCase = getWishlistUseCase,
        removeFromWishlist = removeFromWishlistUseCase,
        wishlistUiFactory = wishlistUiFactory,
        savedStateHandle = savedStateHandle
    )
}