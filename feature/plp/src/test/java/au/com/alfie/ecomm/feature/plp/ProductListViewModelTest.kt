package au.com.alfie.ecomm.feature.plp

import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.productDetailsNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.productlist.ProductListNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.productlist.ProductListType
import au.com.alfie.ecomm.core.navigation.arguments.productlist.productListNavArgs
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.domain.usecase.productlist.GetPaginatedProductListUseCase
import au.com.alfie.ecomm.domain.usecase.productlist.GetProductListLayoutModeUseCase
import au.com.alfie.ecomm.domain.usecase.productlist.UpdateProductListLayoutModeUseCase
import au.com.alfie.ecomm.feature.plp.factory.ProductListEntryUIFactory
import au.com.alfie.ecomm.feature.plp.factory.ProductListUIFactory
import au.com.alfie.ecomm.feature.plp.model.ProductListEvent
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import au.com.alfie.ecomm.repository.productlist.model.ProductListMetadata
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.invoke
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse

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
    private lateinit var savedStateHandle: SavedStateHandle

    @RelaxedMockK
    private lateinit var uiEventEmitterDelegate: UIEventEmitterDelegate

    @BeforeEach
    fun setUp() {
        mockkStatic("au.com.alfie.ecomm.feature.plp.NavArgsGettersKt")
        every { savedStateHandle.navArgs<ProductListNavArgs>() } returns productListNavArgs(
            type = ProductListType.Search("query")
        )
        products.forEachIndexed { index, product ->
            coEvery { entryUiFactory(product, ProductListLayoutMode.GRID, any()) } returns productsMediumUI[index]
            coEvery { entryUiFactory(product, ProductListLayoutMode.COLUMN, any()) } returns productsLargeUI[index]
        }
        every { getPaginatedProductListUseCase(any(), any(), any(), any(), any()) } returns Pager(
            config = pagerConfig,
            initialKey = null,
            pagingSourceFactory = products.asPagingSourceFactory()
        ).flow
    }

    @Test
    fun `init - WHEN layout mode is GRID THEN products are correctly loaded and mapped to Medium product cards`() = runTest {
        coEvery { getProductListLayoutModeUseCase() } returns ProductListLayoutMode.GRID
        coEvery { productListUIFactory(any(), ProductListLayoutMode.GRID) } returns gridProductListUI

        val viewModel = buildViewModel()

        val result = viewModel.productPager.asSnapshot()

        assertEquals(productsMediumUI, result)
    }

    @Test
    fun `init - WHEN layout mode is COLUMN THEN products are correctly loaded and mapped to Large product cards`() = runTest {
        coEvery { getProductListLayoutModeUseCase() } returns ProductListLayoutMode.COLUMN
        coEvery { productListUIFactory(any(), ProductListLayoutMode.COLUMN) } returns columnProductListUI

        val viewModel = buildViewModel()

        val result = viewModel.productPager.asSnapshot()

        assertEquals(productsLargeUI, result)
    }

    @Test
    fun `init - WHEN metadata is received THEN correctly sets the state`() = runTest {
        coEvery { getProductListLayoutModeUseCase() } returns ProductListLayoutMode.GRID
        coEvery { productListUIFactory(any(), ProductListLayoutMode.GRID) } returns gridProductListUI
        coEvery { productListUIFactory(any(), metadata = any()) } returns gridProductListUI
        every { getPaginatedProductListUseCase(any(), any(), captureLambda(), any(), any()) } answers {
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

            assertEquals("Women", result.title)
            assertEquals(2, result.resultCount)
            assertFalse(result.isLoadingMetadata)
        }
    }

    @Test
    fun `init - when list type is Category Id then categoryId is used`() = runTest {
        every { savedStateHandle.navArgs<ProductListNavArgs>() } returns productListNavArgs(
            type = ProductListType.Category.Id("123456")
        )

        buildViewModel()

        verify { getPaginatedProductListUseCase("123456", null, any(), any(), any()) }
    }

    @Test
    fun `init - when list type is Search then query is used`() = runTest {
        every { savedStateHandle.navArgs<ProductListNavArgs>() } returns productListNavArgs(
            type = ProductListType.Search("query")
        )

        buildViewModel()

        verify { getPaginatedProductListUseCase(null, "query", any(), any(), any()) }
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

    private fun buildViewModel() = ProductListViewModel(
        getPaginatedProductList = getPaginatedProductListUseCase,
        getProductListLayoutMode = getProductListLayoutModeUseCase,
        updateProductListLayoutMode = updateProductListLayoutModeUseCase,
        productListEntryUIFactory = entryUiFactory,
        productListUIFactory = productListUIFactory,
        savedStateHandle = savedStateHandle,
        uiEventEmitterDelegate = uiEventEmitterDelegate
    )
}
