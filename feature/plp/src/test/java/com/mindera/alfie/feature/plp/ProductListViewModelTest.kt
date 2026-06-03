package com.mindera.alfie.feature.plp

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.productlist.ProductListType
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.productlist.GetPaginatedProductListUseCase
import com.mindera.alfie.domain.usecase.productlist.GetProductListLayoutModeUseCase
import com.mindera.alfie.domain.usecase.productlist.UpdateProductListLayoutModeUseCase
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistIdsUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
import com.mindera.alfie.feature.plp.factory.ProductListEntryUIFactory
import com.mindera.alfie.feature.plp.factory.ProductListUIFactory
import com.mindera.alfie.feature.plp.model.ProductListEvent
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.productlist.model.ProductListMetadata
import com.mindera.alfie.repository.productlist.model.ProductSortOption
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.invoke
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class ProductListViewModelTest {

    companion object {
        private val pagerConfig = PagingConfig(pageSize = 15)
    }

    @RelaxedMockK
    private lateinit var getPaginatedProductListUseCase: GetPaginatedProductListUseCase

    @RelaxedMockK
    private lateinit var getProductListLayoutModeUseCase: GetProductListLayoutModeUseCase

    @RelaxedMockK
    private lateinit var updateProductListLayoutModeUseCase: UpdateProductListLayoutModeUseCase

    @RelaxedMockK
    private lateinit var entryUiFactory: ProductListEntryUIFactory

    @RelaxedMockK
    private lateinit var productListUIFactory: ProductListUIFactory

    @RelaxedMockK
    private lateinit var uiEventEmitterDelegate: UIEventEmitterDelegate

    @RelaxedMockK
    private lateinit var addToWishlistUseCase: AddToWishlistUseCase

    @RelaxedMockK
    private lateinit var removeFromWishlistUseCase: RemoveFromWishlistUseCase

    @RelaxedMockK
    private lateinit var getWishlistIdsUseCase: GetWishlistIdsUseCase

    @RelaxedMockK
    private lateinit var context: Context

    @BeforeEach
    fun setUp() {
        coEvery { productListUIFactory(any(), any<ProductListLayoutMode>()) } answers { firstArg() }
        products.forEachIndexed { index, product ->
            coEvery { entryUiFactory(product, any(), any()) } returns productsVerticalUI[index]
        }
        every { getPaginatedProductListUseCase(any(), any(), any(), any(), any()) } returns Pager(
            config = pagerConfig,
            initialKey = null,
            pagingSourceFactory = products.asPagingSourceFactory()
        ).flow
    }

    @Test
    fun `init - WHEN layout mode is GRID THEN products are correctly loaded and mapped to product cards`() = runTest {
        coEvery { getProductListLayoutModeUseCase() } returns ProductListLayoutMode.GRID
        coEvery { productListUIFactory(any(), ProductListLayoutMode.GRID) } returns gridProductListUI

        val viewModel = buildViewModel()

        val result = viewModel.productPager.asSnapshot()

        assertEquals(productsVerticalUI, result)
    }

    @Test
    fun `init - WHEN layout mode is COLUMN THEN products are correctly loaded and mapped to product cards`() = runTest {
        coEvery { getProductListLayoutModeUseCase() } returns ProductListLayoutMode.COLUMN
        coEvery { productListUIFactory(any(), ProductListLayoutMode.COLUMN) } returns columnProductListUI

        val viewModel = buildViewModel()

        val result = viewModel.productPager.asSnapshot()

        assertEquals(productsVerticalUI, result)
    }

    @Test
    fun `init - WHEN metadata is received THEN correctly sets the state`() = runTest {
        coEvery { getProductListLayoutModeUseCase() } returns ProductListLayoutMode.GRID
        coEvery { productListUIFactory(any(), ProductListLayoutMode.GRID) } returns gridProductListUI
        coEvery { productListUIFactory(any(), metadata = any()) } returns gridProductListUI
        every { getPaginatedProductListUseCase(any(), any(), any(), captureLambda(), any()) } answers {
            lambda<(ProductListMetadata) -> Unit>().invoke(productListMetadata)
            Pager(
                config = pagerConfig,
                initialKey = null,
                pagingSourceFactory = products.asPagingSourceFactory()
            ).flow
        }

        val viewModel = buildViewModel()

        viewModel.state.test {
            val result = awaitItem()

            assertEquals(2, result.resultCount)
            assertFalse(result.isLoadingMetadata)
        }
    }

    @Test
    fun `init - THEN uses hardcoded women collection handle`() = runTest {
        buildViewModel()

        @Suppress("UnusedFlow")
        verify { getPaginatedProductListUseCase("women", any(), any(), any(), any()) }
    }

    @Test
    fun `init - THEN default sort is RECOMMENDED`() = runTest {
        val viewModel = buildViewModel()

        viewModel.state.test {
            val result = awaitItem()
            assertEquals(ProductSortOption.RECOMMENDED, result.selectedSort)
        }
    }

    @Test
    fun `init - THEN default filters are null`() = runTest {
        val viewModel = buildViewModel()

        viewModel.state.test {
            val result = awaitItem()
            assertNull(result.selectedFilters)
        }
    }

    @Test
    fun `checkLayoutModePreference - WHEN layout mode is GRID THEN correctly sets the state`() = runTest {
        coEvery { getProductListLayoutModeUseCase() } returns ProductListLayoutMode.GRID
        coEvery { productListUIFactory(any(), ProductListLayoutMode.GRID) } returns gridProductListUI

        val viewModel = buildViewModel()

        viewModel.state.test {
            val result = awaitItem()

            assertEquals(ProductListLayoutMode.GRID, result.layoutMode)
            assertEquals(2, result.compactColumnCount)
            assertEquals(3, result.nonCompactColumnCount)
        }
    }

    @Test
    fun `checkLayoutModePreference - WHEN layout mode is COLUMN THEN correctly sets the state`() = runTest {
        coEvery { getProductListLayoutModeUseCase() } returns ProductListLayoutMode.COLUMN
        coEvery { productListUIFactory(any(), ProductListLayoutMode.COLUMN) } returns columnProductListUI

        val viewModel = buildViewModel()

        viewModel.state.test {
            val result = awaitItem()

            assertEquals(ProductListLayoutMode.COLUMN, result.layoutMode)
            assertEquals(1, result.compactColumnCount)
            assertEquals(2, result.nonCompactColumnCount)
        }
    }

    @Test
    fun `handleEvent - GIVEN OpenProduct THEN calls navigation to Product Details`() = runTest {
        val expected = Screen.ProductDetails(productDetailsNavArgs("123456"))
        val event = ProductListEvent.OpenProduct(productId = "123456")

        val viewModel = buildViewModel()

        viewModel.handleEvent(event)

        viewModel.run {
            verify { navigateTo(screen = expected) }
        }
    }

    @Test
    fun `handleEvent - GIVEN ChangeLayoutMode WHEN layout mode is GRID THEN correctly sets the state`() = runTest {
        coEvery { getProductListLayoutModeUseCase() } returns ProductListLayoutMode.COLUMN
        coEvery { productListUIFactory(any(), ProductListLayoutMode.GRID) } returns gridProductListUI
        coEvery { productListUIFactory(any(), ProductListLayoutMode.COLUMN) } returns columnProductListUI
        coEvery { updateProductListLayoutModeUseCase(any()) } returns UseCaseResult.Success(Unit)

        val viewModel = buildViewModel()

        viewModel.state.test {
            awaitItem() // Initial state
            viewModel.handleEvent(ProductListEvent.ChangeLayoutMode(ProductListLayoutMode.GRID))

            val result = awaitItem()

            assertEquals(ProductListLayoutMode.GRID, result.layoutMode)
            assertEquals(2, result.compactColumnCount)
            assertEquals(3, result.nonCompactColumnCount)
        }
    }

    @Test
    fun `handleEvent - GIVEN ChangeLayoutMode WHEN layout mode is COLUMN THEN correctly sets the state`() = runTest {
        coEvery { getProductListLayoutModeUseCase() } returns ProductListLayoutMode.GRID
        coEvery { productListUIFactory(any(), ProductListLayoutMode.GRID) } returns gridProductListUI
        coEvery { productListUIFactory(any(), ProductListLayoutMode.COLUMN) } returns columnProductListUI
        coEvery { updateProductListLayoutModeUseCase(any()) } returns UseCaseResult.Success(Unit)

        val viewModel = buildViewModel()

        viewModel.state.test {
            awaitItem() // Initial state
            viewModel.handleEvent(ProductListEvent.ChangeLayoutMode(ProductListLayoutMode.COLUMN))

            val result = awaitItem()

            assertEquals(ProductListLayoutMode.COLUMN, result.layoutMode)
            assertEquals(1, result.compactColumnCount)
            assertEquals(2, result.nonCompactColumnCount)
        }
    }

    @Test
    fun `handleEvent - GIVEN ApplySort THEN updates state and restarts pager`() = runTest {
        val viewModel = buildViewModel()

        viewModel.state.test {
            awaitItem() // Initial state
            viewModel.handleEvent(ProductListEvent.ApplySort(ProductSortOption.HIGHEST_PRICE))

            val result = awaitItem()
            assertEquals(ProductSortOption.HIGHEST_PRICE, result.selectedSort)
        }
    }

    @Test
    fun `handleEvent - GIVEN ApplyFilters THEN updates state`() = runTest {
        val filters = ProductListFilter(brandNames = listOf("Brand"), minPrice = null, maxPrice = null, productTypes = null)
        val viewModel = buildViewModel()

        viewModel.state.test {
            awaitItem() // Initial state
            viewModel.handleEvent(ProductListEvent.ApplyFilters(filters))

            val result = awaitItem()
            assertEquals(filters, result.selectedFilters)
        }
    }

    @Test
    fun `handleEvent - GIVEN ApplyFilters with null THEN clears filters`() = runTest {
        val filters = ProductListFilter(brandNames = listOf("Brand"), minPrice = null, maxPrice = null, productTypes = null)
        val viewModel = buildViewModel()

        viewModel.state.test {
            awaitItem() // Initial state
            viewModel.handleEvent(ProductListEvent.ApplyFilters(filters))
            awaitItem() // State with filters applied
            viewModel.handleEvent(ProductListEvent.ApplyFilters(null))

            val result = awaitItem()
            assertNull(result.selectedFilters)
        }
    }

    @Test
    fun `handleEvent - GIVEN OpenFilters THEN showRefine becomes true`() = runTest {
        val viewModel = buildViewModel()

        viewModel.state.test {
            awaitItem() // Initial state
            viewModel.handleEvent(ProductListEvent.OpenFilters)

            val result = awaitItem()
            assertTrue(result.showRefine)
        }
    }

    @Test
    fun `handleEvent - GIVEN DismissRefine THEN showRefine becomes false`() = runTest {
        val viewModel = buildViewModel()

        viewModel.state.test {
            awaitItem() // Initial state
            viewModel.handleEvent(ProductListEvent.OpenFilters)
            awaitItem() // showRefine = true
            viewModel.handleEvent(ProductListEvent.DismissRefine)

            val result = awaitItem()
            assertFalse(result.showRefine)
        }
    }

    private fun buildViewModel(
        type: ProductListType = ProductListType.Category.Slug("women")
    ) = ProductListViewModel(
        savedStateHandle = SavedStateHandle(mapOf("type" to type)),
        getPaginatedProductList = getPaginatedProductListUseCase,
        getProductListLayoutMode = getProductListLayoutModeUseCase,
        updateProductListLayoutMode = updateProductListLayoutModeUseCase,
        productListEntryUIFactory = entryUiFactory,
        productListUIFactory = productListUIFactory,
        uiEventEmitterDelegate = uiEventEmitterDelegate,
        addToWishlistUseCase = addToWishlistUseCase,
        removeWishlistUseCase = removeFromWishlistUseCase,
        getWishlistIds = getWishlistIdsUseCase,
        context = context
    )
}
