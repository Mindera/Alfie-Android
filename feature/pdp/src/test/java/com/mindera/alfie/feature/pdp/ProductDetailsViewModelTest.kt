package com.mindera.alfie.feature.pdp

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.ProductDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.webview.webViewNavArgs
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.bag.AddToBagUseCase
import com.mindera.alfie.domain.usecase.product.GetProductUseCase
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistIdsUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
import com.mindera.alfie.feature.pdp.model.ProductDetailsEvent
import com.mindera.alfie.feature.pdp.model.ProductDetailsSectionItem
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState
import com.mindera.alfie.feature.pdp.model.ShareEvent
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class ProductDetailsViewModelTest {

    @RelaxedMockK
    private lateinit var addToBagUseCase: AddToBagUseCase

    @RelaxedMockK
    private lateinit var getWishlistIds: GetWishlistIdsUseCase

    @RelaxedMockK
    private lateinit var addToWishlistUseCase: AddToWishlistUseCase

    @RelaxedMockK
    private lateinit var removeWishlistUseCase: RemoveFromWishlistUseCase

    @RelaxedMockK
    private lateinit var getProductUseCase: GetProductUseCase

    @RelaxedMockK
    private lateinit var productDetailsUIFactory: ProductDetailsUIFactory

    @RelaxedMockK
    private lateinit var savedStateHandle: SavedStateHandle

    @RelaxedMockK
    private lateinit var context: Context

    @BeforeEach
    fun setUp() {
        mockkStatic("com.mindera.alfie.feature.pdp.NavArgsGettersKt")
        every { savedStateHandle.navArgs<ProductDetailsNavArgs>() } returns productDetailsNavArgs(id = "123456")

        coEvery { getProductUseCase(any()) } returns UseCaseResult.Success(product)
        coEvery { productDetailsUIFactory(any()) } returns productDetailsUI
    }

    @Test
    fun `init - state is successfully defined with the uiFactory result`() = runTest {
        val expected = ProductDetailsUIState.Data.Loaded(productDetailsUI)
        val viewModel = buildViewModel()

        viewModel.state.test {
            val result = awaitItem()
            assertEquals(expected, result)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `handleEvent - GIVEN OnColourClick WHEN colour is valid THEN colour is selected`() = runTest {
        val event = ProductDetailsEvent.OnColorClick(0)
        val viewModel = buildViewModel()
        viewModel.uiEvent.test {
            viewModel.handleEvent(event)
            coVerify { productDetailsUIFactory(product) }
            assertIs<ProductDetailsUIState.Data.Loaded>(viewModel.state.value)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `handleEvent - GIVEN OnSectionClick THEN should open webView with url and title`() = runTest {
        val item = ProductDetailsSectionItem(
            title = StringResource.fromId(R.string.product_details_section_delivery_and_returns),
            url = ProductDetailsUIFactory.DELIVERY_RETURNS_URL
        )
        val args = webViewNavArgs(
            url = item.url,
            title = item.title
        )
        val uiEvent = UIEvent.Base.NavigateToScreen(screen = Screen.WebView(args = args))
        val event = ProductDetailsEvent.OnSectionClick(item)
        val viewModel = buildViewModel()

        viewModel.uiEvent.test {
            viewModel.handleEvent(event)

            val result = expectMostRecentItem()
            assertTrue(result is UIEvent.Base.NavigateToScreen)
            assertEquals(uiEvent.screen, result.screen)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `handleEvent - GIVEN OnShareClick THEN share action is created`() = runTest {
        val event = ProductDetailsEvent.OnShareClick
        val viewModel = buildViewModel()
        viewModel.uiEvent.test {
            assertIs<ProductDetailsUIState.Data.Loaded>(viewModel.state.value)
            viewModel.handleEvent(event)

            val result = expectMostRecentItem()
            assertIs<ShareEvent>(result)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `handleEvent - GIVEN OnAddToBagClick THEN share action is created`() = runTest {
        val event = ProductDetailsEvent.OnAddToBagClick
        val viewModel = buildViewModel()
        viewModel.uiEvent.test {
            viewModel.handleEvent(event)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `handleEvent - GIVEN OnSizeSelect WHEN size is valid THEN size is selected`() = runTest {
        val event = ProductDetailsEvent.OnSizeSelect(sizeUI = sizeUI)
        val viewModel = buildViewModel()
        viewModel.uiEvent.test {
            val details = (viewModel.state.value as? ProductDetailsUIState.Data.Loaded)?.details
            viewModel.handleEvent(event)
            coVerify { productDetailsUIFactory.setSelectedSize(details = details ?: ProductDetailsUIFactory.LOADING, sizeUI = sizeUI) }
            assertIs<ProductDetailsUIState.Data.Loaded>(viewModel.state.value)
            cancelAndConsumeRemainingEvents()
        }
    }

    private fun buildViewModel() = ProductDetailsViewModel(
        addToBagUseCase = addToBagUseCase,
        getProductUseCase = getProductUseCase,
        getWishlistIds = getWishlistIds,
        addToWishlistUseCase = addToWishlistUseCase,
        removeWishlistUseCase = removeWishlistUseCase,
        uiFactory = productDetailsUIFactory,
        savedStateHandle = savedStateHandle,
        uiEventEmitterDelegate = UIEventEmitterDelegate(),
        context = context
    )
}
