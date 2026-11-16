package com.mindera.alfie.feature.bag

import android.content.Context
import app.cash.turbine.test
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomVisuals
import com.mindera.alfie.designsystem.component.snackbar.SnackbarType
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.bag.AddToBagUseCase
import com.mindera.alfie.domain.usecase.bag.GetBagUseCase
import com.mindera.alfie.domain.usecase.bag.RefreshBagUseCase
import com.mindera.alfie.domain.usecase.bag.RemoveFromBagUseCase
import com.mindera.alfie.domain.usecase.product.GetProductUseCase
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.bag.Bag
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class BagViewModelTest {

    @RelaxedMockK
    private lateinit var getBagUseCase: GetBagUseCase

    @RelaxedMockK
    private lateinit var refreshBagUseCase: RefreshBagUseCase

    @RelaxedMockK
    private lateinit var removeFromBagUseCase: RemoveFromBagUseCase

    @RelaxedMockK
    private lateinit var addToBagUseCase: AddToBagUseCase

    @RelaxedMockK
    private lateinit var getProductUseCase: GetProductUseCase

    @RelaxedMockK
    private lateinit var addToWishlistUseCase: AddToWishlistUseCase

    @RelaxedMockK
    private lateinit var uiEventEmitterDelegate: UIEventEmitterDelegate

    @RelaxedMockK
    private lateinit var context: Context

    private val bagResults = MutableStateFlow<UseCaseResult<Bag>>(UseCaseResult.Success(bag))

    @BeforeEach
    fun setUp() {
        every { getBagUseCase() } returns bagResults
        coEvery { getProductUseCase(any()) } answers {
            productsBySlug[firstArg()]?.let { UseCaseResult.Success(it) } ?: UseCaseResult.Error(mockk())
        }
    }

    @Test
    fun `WHEN the Bag loads THEN every line shows`() = runTest {
        val viewModel = buildViewModel()
        delay(300)

        val loaded = assertIs<BagUiState.Data.Loaded>(viewModel.state.value)
        assertEquals(listOf("line-1", "line-2"), loaded.content.items.map { it.id })
        assertEquals("$200", loaded.content.summary.totalFormatted)
    }

    @Test
    fun `WHEN the Bag is empty THEN show the empty state`() = runTest {
        bagResults.value = UseCaseResult.Success(Bag.EMPTY)

        val viewModel = buildViewModel()
        delay(300)

        assertEquals(BagUiState.Data.Empty, viewModel.state.value)
    }

    @Test
    fun `WHEN the Cart can't be fetched THEN show the error state, never empty`() = runTest {
        bagResults.value = UseCaseResult.Error(mockk())

        val viewModel = buildViewModel()
        delay(300)

        assertEquals(BagUiState.Error, viewModel.state.value)
    }

    @Test
    fun `WHEN a product fails to load THEN its line still shows from the Cart`() = runTest {
        coEvery { getProductUseCase("123456-product") } returns UseCaseResult.Error(mockk())

        val viewModel = buildViewModel()
        delay(300)

        val items = assertIs<BagUiState.Data.Loaded>(viewModel.state.value).content.items
        assertEquals(listOf("Cart name 1", "Product 2"), items.map { it.productCardData.name })
    }

    @Test
    fun `WHEN the Bag changes THEN only new products are fetched`() = runTest {
        buildViewModel()
        delay(300)

        bagResults.value = UseCaseResult.Success(bag.copy(lines = bagLines.reversed()))
        delay(300)

        coVerify(exactly = 1) { getProductUseCase("123456-product") }
        coVerify(exactly = 1) { getProductUseCase("654321-product") }
    }

    @Test
    fun `onRetry - fetches the Cart again`() = runTest {
        val viewModel = buildViewModel()

        viewModel.onRetry()
        delay(300)

        coVerify(exactly = 1) { refreshBagUseCase() }
    }

    @Test
    fun `onRemoveClicked - hides the line while the remove is in flight`() = runTest {
        coEvery { removeFromBagUseCase("line-1") } coAnswers {
            delay(1_000)
            UseCaseResult.Success(bag.copy(lines = listOf(bagLines[1])))
        }
        val viewModel = buildViewModel()
        delay(300)

        viewModel.onRemoveClicked(bagLines[0])
        delay(100)

        val items = assertIs<BagUiState.Data.Loaded>(viewModel.state.value).content.items
        assertEquals(listOf("line-2"), items.map { it.id })
    }

    @Test
    fun `onRemoveClicked - WHEN the remove fails THEN the line comes back with an error`() = runTest {
        coEvery { removeFromBagUseCase(any()) } returns UseCaseResult.Error(mockk())
        val viewModel = buildViewModel(emitter = UIEventEmitterDelegate())
        delay(300)

        viewModel.uiEvent.test {
            viewModel.onRemoveClicked(bagLines[0])

            val visuals = (awaitItem() as UIEvent.Base.ShowSnackbar).visuals
            assertEquals(SnackbarType.Error, assertIs<SnackbarCustomVisuals.Snackbar>(visuals).type)
            cancelAndConsumeRemainingEvents()
        }
        val items = assertIs<BagUiState.Data.Loaded>(viewModel.state.value).content.items
        assertEquals(listOf("line-1", "line-2"), items.map { it.id })
    }

    @Test
    fun `onRemoveClicked - WHEN the last line is removed THEN show the empty state`() = runTest {
        bagResults.value = UseCaseResult.Success(bag.copy(lines = listOf(bagLines[0])))
        coEvery { removeFromBagUseCase(any()) } coAnswers {
            delay(1_000)
            UseCaseResult.Success(Bag.EMPTY)
        }
        val viewModel = buildViewModel()
        delay(300)

        viewModel.onRemoveClicked(bagLines[0])
        delay(100)

        assertEquals(BagUiState.Data.Empty, viewModel.state.value)
    }

    @Test
    fun `onRemoveClicked - undo adds the same variant and quantity back`() = runTest {
        val line = bagLines[0].copy(quantity = 3)
        coEvery { removeFromBagUseCase(any()) } returns UseCaseResult.Success(Bag.EMPTY)
        coEvery { addToBagUseCase(any(), any(), any()) } returns UseCaseResult.Success(bag)
        val viewModel = buildViewModel(emitter = UIEventEmitterDelegate())

        viewModel.uiEvent.test {
            viewModel.onRemoveClicked(line)

            val visuals = (awaitItem() as UIEvent.Base.ShowSnackbar).visuals
            assertIs<SnackbarCustomVisuals.Toast>(visuals)
            visuals.onActionClick()
            delay(300)

            coVerify(exactly = 1) { addToBagUseCase(variantId = "variant1Id", productId = "123456", quantity = 3) }
            expectNoEvents()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onRemoveClicked - WHEN the undo fails THEN it says so`() = runTest {
        coEvery { removeFromBagUseCase(any()) } returns UseCaseResult.Success(Bag.EMPTY)
        coEvery { addToBagUseCase(any(), any(), any()) } returns UseCaseResult.Error(mockk())
        val viewModel = buildViewModel(emitter = UIEventEmitterDelegate())

        viewModel.uiEvent.test {
            viewModel.onRemoveClicked(bagLines[0])
            (awaitItem() as UIEvent.Base.ShowSnackbar).visuals.onActionClick()

            val visuals = (awaitItem() as UIEvent.Base.ShowSnackbar).visuals
            assertEquals(SnackbarType.Error, assertIs<SnackbarCustomVisuals.Snackbar>(visuals).type)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onSaveClicked - adds the product to the wishlist by slug and leaves the Bag alone`() = runTest {
        val viewModel = buildViewModel()

        viewModel.onSaveClicked(bagLines[0])
        delay(300)

        coVerify(exactly = 1) { addToWishlistUseCase("123456-product") }
        coVerify(exactly = 0) { removeFromBagUseCase(any()) }
    }

    // The snackbar tests need the events the delegate actually emits, so they pass a real one.
    private fun buildViewModel(
        emitter: UIEventEmitterDelegate = uiEventEmitterDelegate
    ) = BagViewModel(
        getBagUseCase = getBagUseCase,
        refreshBagUseCase = refreshBagUseCase,
        removeFromBagUseCase = removeFromBagUseCase,
        addToBagUseCase = addToBagUseCase,
        addToWishlistUseCase = addToWishlistUseCase,
        getProductUseCase = getProductUseCase,
        bagUiFactory = BagUiFactory(),
        context = context,
        uiEventEmitterDelegate = emitter
    )
}
