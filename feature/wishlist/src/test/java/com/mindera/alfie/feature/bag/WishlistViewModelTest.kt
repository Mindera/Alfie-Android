package com.mindera.alfie.feature.bag

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.feature.wishlist.WishlistUIFactory
import com.mindera.alfie.feature.wishlist.WishlistUiState
import com.mindera.alfie.feature.wishlist.WishlistViewModel
import com.mindera.alfie.repository.product.model.Product
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
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

    @RelaxedMockK
    private lateinit var uiEventEmitterDelegate: UIEventEmitterDelegate

    @Test
    fun `WHEN getWishlistList returns a success THEN update the state with the correct wishlist items`() =
        runTest {
            val savedStateHandle = buildSavedStateHandle()

            coEvery { getWishlistUseCase() } returns flow {
                emit(UseCaseResult.Success(products))
            }
            every { wishlistUiFactory(products, any(), any(), any()) } returns wishListProductUi

            val viewModel = buildViewModel(savedStateHandle)

            viewModel.state.test {
                val result = awaitItem()
                assertEquals(WishlistUiState.Data.Loaded(wishListProductUi), result)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN getWishlistList returns an error THEN update the state with the error state`() =
        runTest {
            val savedStateHandle = buildSavedStateHandle()

            coEvery { getWishlistUseCase() } returns flow {
                emit(UseCaseResult.Error(mockk()))
            }

            val viewModel = buildViewModel(savedStateHandle)

            viewModel.state.test {
                val result = awaitItem()
                assertEquals(WishlistUiState.Error, result)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN onNavigateToProductDetails is called THEN navigates to ProductDetails screen with the correct productId`() =
        runTest {
            val savedStateHandle = buildSavedStateHandle()
            val productId = "123456"
            val expected = Screen.ProductDetails(args = productDetailsNavArgs(id = productId))

            val viewModel = buildViewModel(savedStateHandle)

            viewModel.onNavigateToProductDetails(productId)

            viewModel.run {
                verify { navigateTo(screen = expected) }
            }
        }

    @Test
    fun `WHEN wishlist loads THEN onAddToBagClick triggers navigation to ProductDetails`() =
        runTest {
            val savedStateHandle = buildSavedStateHandle()
            val product = products.first()
            val expected = Screen.ProductDetails(args = productDetailsNavArgs(id = product.id))
            val onAddToBagClickSlot = slot<ClickEventOneArg<Product>>()

            coEvery { getWishlistUseCase() } returns flow {
                emit(UseCaseResult.Success(products))
            }
            every {
                wishlistUiFactory(products, any(), capture(onAddToBagClickSlot), any())
            } returns wishListProductUi

            val viewModel = buildViewModel(savedStateHandle)
            onAddToBagClickSlot.captured(product)

            viewModel.run {
                verify { navigateTo(screen = expected) }
            }
        }

    @Test
    fun `WHEN wishlist loads THEN onProductClick triggers navigation to ProductDetails`() =
        runTest {
            val savedStateHandle = buildSavedStateHandle()
            val product = products.first()
            val expected = Screen.ProductDetails(args = productDetailsNavArgs(id = product.id))
            val onProductClickSlot = slot<ClickEventOneArg<Product>>()

            coEvery { getWishlistUseCase() } returns flow {
                emit(UseCaseResult.Success(products))
            }
            every {
                wishlistUiFactory(products, any(), any(), capture(onProductClickSlot))
            } returns wishListProductUi

            val viewModel = buildViewModel(savedStateHandle)
            onProductClickSlot.captured(product)

            viewModel.run {
                verify { navigateTo(screen = expected) }
            }
        }

    private fun buildSavedStateHandle() = mockk<SavedStateHandle>(relaxed = true).also {
        every { it.get<Boolean>("launchFromTop") } returns false
    }

    private fun buildViewModel(savedStateHandle: SavedStateHandle) = WishlistViewModel(
        getWishlistUseCase = getWishlistUseCase,
        removeFromWishlist = removeFromWishlistUseCase,
        wishlistUiFactory = wishlistUiFactory,
        savedStateHandle = savedStateHandle,
        uiEventEmitterDelegate = uiEventEmitterDelegate
    )
}