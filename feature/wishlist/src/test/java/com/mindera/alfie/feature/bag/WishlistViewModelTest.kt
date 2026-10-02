package com.mindera.alfie.feature.bag

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomVisuals
import com.mindera.alfie.designsystem.component.snackbar.SnackbarType
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.feature.wishlist.WishlistUIFactory
import com.mindera.alfie.feature.wishlist.WishlistUiState
import com.mindera.alfie.feature.wishlist.WishlistViewModel
import com.mindera.alfie.repository.product.model.Product
import io.mockk.coEvery
import io.mockk.coVerify
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
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class WishlistViewModelTest {

    @RelaxedMockK
    private lateinit var getWishlistUseCase: GetWishlistUseCase

    @RelaxedMockK
    private lateinit var removeFromWishlistUseCase: RemoveFromWishlistUseCase

    @RelaxedMockK
    private lateinit var addToWishlistUseCase: AddToWishlistUseCase

    @RelaxedMockK
    private lateinit var wishlistUiFactory: WishlistUIFactory

    @RelaxedMockK
    private lateinit var context: Context

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
            val expected = Screen.ProductDetails(args = productDetailsNavArgs(handle = productId))

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
            val expected = Screen.ProductDetails(args = productDetailsNavArgs(handle = product.slug))
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
            val expected = Screen.ProductDetails(args = productDetailsNavArgs(handle = product.slug))
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

    @Test
    fun `WHEN a card's heart is tapped THEN the product is removed and a toast offers undo`() =
        runTest {
            val product = products.first()
            val onRemoveClickSlot = slot<ClickEventOneArg<Product>>()
            coEvery { getWishlistUseCase() } returns flow { emit(UseCaseResult.Success(products)) }
            every { wishlistUiFactory(products, capture(onRemoveClickSlot), any(), any()) } returns wishListProductUi
            coEvery { removeFromWishlistUseCase(any()) } returns UseCaseResult.Success(Unit)
            val emitter = UIEventEmitterDelegate()
            val viewModel = buildViewModel(buildSavedStateHandle(), emitter)

            viewModel.uiEvent.test {
                onRemoveClickSlot.captured(product)

                val visuals = (awaitItem() as UIEvent.Base.ShowSnackbar).visuals
                assertIs<SnackbarCustomVisuals.Toast>(visuals)
                coVerify(exactly = 1) { removeFromWishlistUseCase(product.slug) }
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN undo is tapped on the toast THEN the product is wishlisted again`() =
        runTest {
            val product = products.first()
            val onRemoveClickSlot = slot<ClickEventOneArg<Product>>()
            coEvery { getWishlistUseCase() } returns flow { emit(UseCaseResult.Success(products)) }
            every { wishlistUiFactory(products, capture(onRemoveClickSlot), any(), any()) } returns wishListProductUi
            coEvery { removeFromWishlistUseCase(any()) } returns UseCaseResult.Success(Unit)
            coEvery { addToWishlistUseCase(any()) } returns UseCaseResult.Success(Unit)
            val emitter = UIEventEmitterDelegate()
            val viewModel = buildViewModel(buildSavedStateHandle(), emitter)

            viewModel.uiEvent.test {
                onRemoveClickSlot.captured(product)
                (awaitItem() as UIEvent.Base.ShowSnackbar).visuals.onActionClick()

                coVerify(exactly = 1) { addToWishlistUseCase(product.slug) }
                expectNoEvents()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN the removal fails THEN it says so instead of failing silently`() =
        runTest {
            val onRemoveClickSlot = slot<ClickEventOneArg<Product>>()
            coEvery { getWishlistUseCase() } returns flow { emit(UseCaseResult.Success(products)) }
            every { wishlistUiFactory(products, capture(onRemoveClickSlot), any(), any()) } returns wishListProductUi
            coEvery { removeFromWishlistUseCase(any()) } returns UseCaseResult.Error(mockk())
            val emitter = UIEventEmitterDelegate()
            val viewModel = buildViewModel(buildSavedStateHandle(), emitter)

            viewModel.uiEvent.test {
                onRemoveClickSlot.captured(products.first())

                assertEquals(SnackbarType.Error, assertIs<SnackbarCustomVisuals.Snackbar>((awaitItem() as UIEvent.Base.ShowSnackbar).visuals).type)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN the undo fails THEN it says so instead of failing silently`() =
        runTest {
            val onRemoveClickSlot = slot<ClickEventOneArg<Product>>()
            coEvery { getWishlistUseCase() } returns flow { emit(UseCaseResult.Success(products)) }
            every { wishlistUiFactory(products, capture(onRemoveClickSlot), any(), any()) } returns wishListProductUi
            coEvery { removeFromWishlistUseCase(any()) } returns UseCaseResult.Success(Unit)
            coEvery { addToWishlistUseCase(any()) } returns UseCaseResult.Error(mockk())
            val emitter = UIEventEmitterDelegate()
            val viewModel = buildViewModel(buildSavedStateHandle(), emitter)

            viewModel.uiEvent.test {
                onRemoveClickSlot.captured(products.first())
                (awaitItem() as UIEvent.Base.ShowSnackbar).visuals.onActionClick()

                assertEquals(SnackbarType.Error, assertIs<SnackbarCustomVisuals.Snackbar>((awaitItem() as UIEvent.Base.ShowSnackbar).visuals).type)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `WHEN retry is tapped after an error THEN the wishlist loads again`() =
        runTest {
            coEvery { getWishlistUseCase() } returnsMany listOf(
                flow { emit(UseCaseResult.Error(mockk())) },
                flow { emit(UseCaseResult.Success(products)) }
            )
            every { wishlistUiFactory(products, any(), any(), any()) } returns wishListProductUi
            val viewModel = buildViewModel(buildSavedStateHandle())
            assertEquals(WishlistUiState.Error, viewModel.state.value)

            viewModel.onRetry()

            assertEquals(WishlistUiState.Data.Loaded(wishListProductUi), viewModel.state.value)
        }

    private fun buildSavedStateHandle() = mockk<SavedStateHandle>(relaxed = true).also {
        every { it.get<Boolean>("launchFromTop") } returns false
    }

    // The snackbar tests need the events the delegate actually emits, so they pass a real one.
    private fun buildViewModel(
        savedStateHandle: SavedStateHandle,
        emitter: UIEventEmitterDelegate = uiEventEmitterDelegate
    ) = WishlistViewModel(
        getWishlistUseCase = getWishlistUseCase,
        removeFromWishlist = removeFromWishlistUseCase,
        addToWishlist = addToWishlistUseCase,
        wishlistUiFactory = wishlistUiFactory,
        context = context,
        savedStateHandle = savedStateHandle,
        uiEventEmitterDelegate = emitter
    )
}