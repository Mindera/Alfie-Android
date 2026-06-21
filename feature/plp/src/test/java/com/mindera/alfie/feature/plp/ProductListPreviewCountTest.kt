package com.mindera.alfie.feature.plp

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.testing.asPagingSourceFactory
import com.mindera.alfie.core.navigation.arguments.productlist.ProductListType
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.productlist.GetPaginatedProductListUseCase
import com.mindera.alfie.domain.usecase.productlist.GetProductListLayoutModeUseCase
import com.mindera.alfie.domain.usecase.productlist.GetProductListUseCase
import com.mindera.alfie.domain.usecase.productlist.UpdateProductListLayoutModeUseCase
import com.mindera.alfie.domain.usecase.wishlist.AddToWishlistUseCase
import com.mindera.alfie.domain.usecase.wishlist.GetWishlistIdsUseCase
import com.mindera.alfie.domain.usecase.wishlist.RemoveFromWishlistUseCase
import com.mindera.alfie.feature.plp.factory.ProductListEntryUIFactory
import com.mindera.alfie.feature.plp.factory.ProductListUIFactory
import com.mindera.alfie.feature.plp.model.ProductListEvent
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.productlist.model.CursorPagination
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListFilter
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.mindera.alfie.repository.productlist.model.ProductListQuerySource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Focused tests for [ProductListViewModel.collectPreviewResultCount]. Kept separate from
 * [ProductListViewModelTest] so it can drive [Dispatchers.Main] with a [StandardTestDispatcher]
 * (backed by the test scheduler) and advance virtual time through the preview debounce.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class ProductListPreviewCountTest {

    private val previewFilters =
        ProductListFilter(brandNames = listOf("Brand"), minPrice = null, maxPrice = null, productTypes = null)

    @RelaxedMockK
    private lateinit var getPaginatedProductListUseCase: GetPaginatedProductListUseCase

    @RelaxedMockK
    private lateinit var getProductListUseCase: GetProductListUseCase

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
        coEvery { productListUIFactory(any(), metadata = any()) } answers { firstArg() }
        coEvery { getProductListLayoutModeUseCase() } returns ProductListLayoutMode.GRID
        every { getPaginatedProductListUseCase(any(), any(), any(), any(), any()) } returns Pager(
            config = PagingConfig(pageSize = 15),
            initialKey = null,
            pagingSourceFactory = products.asPagingSourceFactory()
        ).flow
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN pending filters equal applied filters THEN emits null without requesting`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.handleEvent(ProductListEvent.OpenFilters)
        viewModel.handleEvent(ProductListEvent.PreviewRefine(filters = null)) // applied filters are null
        advanceUntilIdle()

        assertNull(viewModel.state.value.previewResultCount)
        coVerify(exactly = 0) { getProductListUseCase(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `WHEN filters change THEN count is emitted only after the debounce`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        coEvery {
            getProductListUseCase(after = null, source = ProductListQuerySource.Collection("frontpage"), filters = previewFilters, sort = any(), limit = 1)
        } returns UseCaseResult.Success(
            ProductList(products = emptyList(), pagination = CursorPagination(endCursor = null, hasNextPage = false, totalCount = 42))
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.handleEvent(ProductListEvent.OpenFilters)
        viewModel.handleEvent(ProductListEvent.PreviewRefine(previewFilters))

        advanceTimeBy(299)
        runCurrent()
        assertNull(viewModel.state.value.previewResultCount)

        advanceTimeBy(2)
        runCurrent()
        assertEquals(42, viewModel.state.value.previewResultCount)
    }

    @Test
    fun `WHEN filters change rapidly THEN only the last filter is requested`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val firstFilters =
            ProductListFilter(brandNames = listOf("First"), minPrice = null, maxPrice = null, productTypes = null)
        coEvery { getProductListUseCase(any(), any(), any(), any(), any()) } returns UseCaseResult.Success(
            ProductList(products = emptyList(), pagination = CursorPagination(endCursor = null, hasNextPage = false, totalCount = 7))
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.handleEvent(ProductListEvent.OpenFilters)
        viewModel.handleEvent(ProductListEvent.PreviewRefine(firstFilters))
        advanceTimeBy(100)
        viewModel.handleEvent(ProductListEvent.PreviewRefine(previewFilters))
        advanceUntilIdle()

        assertEquals(7, viewModel.state.value.previewResultCount)
        coVerify(exactly = 1) { getProductListUseCase(null, ProductListQuerySource.Collection("frontpage"), previewFilters, any(), 1) }
        coVerify(exactly = 0) { getProductListUseCase(null, ProductListQuerySource.Collection("frontpage"), firstFilters, any(), 1) }
    }

    @Test
    fun `WHEN the refine sheet is dismissed within the debounce THEN no request is made`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        coEvery { getProductListUseCase(any(), any(), any(), any(), any()) } returns UseCaseResult.Success(
            ProductList(products = emptyList(), pagination = CursorPagination(endCursor = null, hasNextPage = false, totalCount = 5))
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.handleEvent(ProductListEvent.OpenFilters)
        viewModel.handleEvent(ProductListEvent.PreviewRefine(previewFilters))
        advanceTimeBy(100)
        viewModel.handleEvent(ProductListEvent.DismissRefine)
        advanceUntilIdle()

        coVerify(exactly = 0) { getProductListUseCase(any(), any(), any(), any(), any()) }
        assertNull(viewModel.state.value.previewResultCount)
    }

    private fun buildViewModel(
        type: ProductListType = ProductListType.Category.Slug("women")
    ) = ProductListViewModel(
        savedStateHandle = SavedStateHandle(mapOf("type" to type)),
        getPaginatedProductList = getPaginatedProductListUseCase,
        getProductList = getProductListUseCase,
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
