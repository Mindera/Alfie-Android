package com.mindera.alfie.feature.bag

import android.content.Context
import app.cash.turbine.test
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.bag.GetBagUseCase
import com.mindera.alfie.domain.usecase.bag.RemoveAllFromBagUseCase
import com.mindera.alfie.domain.usecase.product.GetProductUseCase
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class BagViewModelTest {

    @RelaxedMockK
    private lateinit var getBagUseCase: GetBagUseCase

    @RelaxedMockK
    private lateinit var getProductUseCase: GetProductUseCase

    @RelaxedMockK
    private lateinit var removeAllFromBagUseCase: RemoveAllFromBagUseCase

    @RelaxedMockK
    private lateinit var addToWishlistUseCase: AddToWishlistUseCase

    @RelaxedMockK
    private lateinit var bagUiFactory: BagUiFactory

    @RelaxedMockK
    private lateinit var uiEventEmitterDelegate: UIEventEmitterDelegate

    @RelaxedMockK
    private lateinit var context: Context

    @Test
    fun `WHEN getBagList returns a success THEN update the state with the correct bag content`() = runTest {
        givenBagLoads()
        coEvery {
            bagUiFactory(
                bagProducts = bagProducts,
                products = products,
                onProductClick = any()
            )
        } returns bagContentUi

        val viewModel = buildViewModel()

        viewModel.state.test {
            delay(300)
            val result = awaitItem()
            assertEquals(BagUiState.Data.Loaded(bagContentUi), result)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN the bag has entries but no product could be loaded THEN show the error state, not empty`() = runTest {
        coEvery { getBagUseCase() } returns flow { emit(UseCaseResult.Success(bagProducts)) }
        // Every product fetch fails, so the factory maps nothing.
        coEvery { getProductUseCase(any()) } returns UseCaseResult.Error(mockk())
        coEvery {
            bagUiFactory(
                bagProducts = bagProducts,
                products = emptyList(),
                onProductClick = any()
            )
        } returns emptyBagContentUi

        val viewModel = buildViewModel()

        viewModel.state.test {
            delay(300)
            val result = awaitItem()
            assertEquals(BagUiState.Error, result)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN the bag itself is empty THEN show the empty state`() = runTest {
        coEvery { getBagUseCase() } returns flow { emit(UseCaseResult.Success(emptyList())) }
        coEvery {
            bagUiFactory(
                bagProducts = emptyList(),
                products = emptyList(),
                onProductClick = any()
            )
        } returns emptyBagContentUi

        val viewModel = buildViewModel()

        viewModel.state.test {
            delay(300)
            val result = awaitItem()
            assertEquals(BagUiState.Data.Empty, result)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN getBagList returns an error THEN update the state with the error state`() = runTest {
        coEvery { getBagUseCase() } returns flowOf(UseCaseResult.Error(mockk()))

        val viewModel = buildViewModel()

        viewModel.state.test {
            val result = awaitItem()
            assertEquals(BagUiState.Error, result)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onRemoveClicked - clears every unit of the line`() = runTest {
        val viewModel = buildViewModel()

        viewModel.onRemoveClicked(bagProducts[0])

        coVerify(exactly = 1) { removeAllFromBagUseCase(bagProducts[0]) }
    }

    @Test
    fun `onSaveClicked - adds the product to the wishlist and leaves the bag alone`() = runTest {
        val viewModel = buildViewModel()

        viewModel.onSaveClicked(bagProducts[0])

        coVerify(exactly = 1) { addToWishlistUseCase(bagProducts[0].productId) }
        coVerify(exactly = 0) { removeAllFromBagUseCase(any()) }
    }

    @Test
    fun `WHEN the same product is in the bag twice THEN its details are fetched once`() = runTest {
        val duplicated = listOf(bagProducts[0], bagProducts[0], bagProducts[1])
        coEvery { getBagUseCase() } returns flow { emit(UseCaseResult.Success(duplicated)) }
        coEvery { getProductUseCase(any()) } answers {
            val productId = firstArg<String>()
            products.find { it.slug == productId }
                ?.let { UseCaseResult.Success(it) }
                ?: UseCaseResult.Error(mockk())
        }

        buildViewModel()
        delay(300)

        coVerify(exactly = 1) { getProductUseCase(bagProducts[0].productId) }
        coVerify(exactly = 1) { getProductUseCase(bagProducts[1].productId) }
    }

    private fun givenBagLoads() {
        coEvery { getBagUseCase() } returns flow { emit(UseCaseResult.Success(bagProducts)) }
        coEvery { getProductUseCase(any()) } answers {
            val productId = firstArg<String>()
            products.find { it.slug == productId }
                ?.let { UseCaseResult.Success(it) }
                ?: UseCaseResult.Error(mockk())
        }
    }

    private fun buildViewModel() = BagViewModel(
        getBagUseCase = getBagUseCase,
        bagUiFactory = bagUiFactory,
        getProductUseCase = getProductUseCase,
        removeAllFromBagUseCase = removeAllFromBagUseCase,
        addToWishlistUseCase = addToWishlistUseCase,
        context = context,
        uiEventEmitterDelegate = uiEventEmitterDelegate
    )
}
