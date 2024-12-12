package au.com.alfie.ecomm.feature.search

import app.cash.turbine.test
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.productDetailsNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.productlist.ProductListType
import au.com.alfie.ecomm.core.navigation.arguments.productlist.productListNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.shop.ShopTab
import au.com.alfie.ecomm.core.navigation.arguments.shop.shopNavArgs
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.domain.usecase.search.ClearRecentSearchesUseCase
import au.com.alfie.ecomm.domain.usecase.search.DeleteRecentSearchUseCase
import au.com.alfie.ecomm.domain.usecase.search.GetRecentSearchesUseCase
import au.com.alfie.ecomm.domain.usecase.search.GetSearchSuggestionsUseCase
import au.com.alfie.ecomm.domain.usecase.search.SaveRecentSearchUseCase
import au.com.alfie.ecomm.feature.search.factory.SearchUIFactory
import au.com.alfie.ecomm.feature.search.model.BrandSuggestionUI
import au.com.alfie.ecomm.feature.search.model.KeywordSuggestionUI
import au.com.alfie.ecomm.feature.search.model.SearchEvent
import au.com.alfie.ecomm.feature.search.model.SearchUIState
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import au.com.alfie.ecomm.repository.search.model.RecentSearch
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class SearchViewModelTest {

    @RelaxedMockK
    private lateinit var getRecentSearches: GetRecentSearchesUseCase

    @RelaxedMockK
    private lateinit var saveRecentSearch: SaveRecentSearchUseCase

    @RelaxedMockK
    private lateinit var deleteRecentSearch: DeleteRecentSearchUseCase

    @RelaxedMockK
    private lateinit var clearRecentSearches: ClearRecentSearchesUseCase

    @RelaxedMockK
    private lateinit var getSearchSuggestions: GetSearchSuggestionsUseCase

    @RelaxedMockK
    private lateinit var searchUIFactory: SearchUIFactory

    @RelaxedMockK
    private lateinit var uiEventEmitterDelegate: UIEventEmitterDelegate

    @InjectMockKs
    private lateinit var viewModel: SearchViewModel

    @BeforeEach
    fun setup() {
        coEvery { getRecentSearches() } returns mockk<Flow<List<RecentSearch>>>()
    }

    @Test
    fun `GIVEN screen start THEN return Empty`() = runTest {
        viewModel.state.test {
            val result = expectMostRecentItem()
            assertIs<SearchUIState.Empty>(result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm WHEN term is empty THEN return Empty`() = runTest {
        val event = SearchEvent.OnUpdateSearchTerm(searchTerm = "")

        viewModel.state.test {
            viewModel.handleEvent(event)

            val state = expectMostRecentItem()
            assertIs<SearchUIState.Empty>(state)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm WHEN term is not empty and result is success THEN return Loading and next Loaded`() = runTest {
        coEvery { getSearchSuggestions(any()) } returns UseCaseResult.Success(searchSuggestions)
        coEvery { searchUIFactory(any(), any()) } returns searchUI
        val expected = SearchUIState.Loaded(searchUI)
        val event = SearchEvent.OnUpdateSearchTerm(searchTerm = "Boot")

        viewModel.state.test {
            assertEquals(SearchUIState.Empty, awaitItem())

            viewModel.handleEvent(event)

            assertEquals(SearchUIState.Loading, awaitItem())

            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm WHEN term is not empty and suggestions are empty THEN return Loading and next Error`() = runTest {
        coEvery { getSearchSuggestions(any()) } returns UseCaseResult.Success(emptySearchSuggestions)
        val event = SearchEvent.OnUpdateSearchTerm(searchTerm = "Boot")

        viewModel.state.test {
            assertEquals(SearchUIState.Empty, awaitItem())

            viewModel.handleEvent(event)

            assertEquals(SearchUIState.Loading, awaitItem())

            assertEquals(SearchUIState.Error("Boot"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm WHEN term is not empty and result is error THEN return Loading and next Error`() = runTest {
        coEvery { getSearchSuggestions(any()) } returns UseCaseResult.Error(mockk())
        val event = SearchEvent.OnUpdateSearchTerm(searchTerm = "Boot")

        viewModel.state.test {
            assertEquals(SearchUIState.Empty, awaitItem())

            viewModel.handleEvent(event)

            assertEquals(SearchUIState.Loading, awaitItem())

            assertEquals(SearchUIState.Error("Boot"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnSearchAction THEN verify use case and navigation are called`() = runTest {
        val searchTerm = "Search"
        val screen = Screen.ProductList(productListNavArgs(ProductListType.Search(searchTerm)))
        val event = SearchEvent.OnSearchAction(searchTerm)

        viewModel.handleEvent(event)

        viewModel.run {
            verify { navigateTo(screen = screen) }
        }
        coVerify { saveRecentSearch(RecentSearch.Query(searchTerm = searchTerm)) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnRecentSearchClick WHEN recent search is query THEN verify use case and navigation are called`() = runTest {
        val searchTerm = "Search"
        val recentSearch = RecentSearch.Query(searchTerm)
        val screen = Screen.ProductList(productListNavArgs(ProductListType.Search(searchTerm)))
        val event = SearchEvent.OnRecentSearchClick(recentSearch)

        viewModel.handleEvent(event)

        viewModel.run {
            verify { navigateTo(screen = screen) }
        }
        coVerify { saveRecentSearch(RecentSearch.Query(searchTerm = searchTerm)) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnRecentSearchClick WHEN recent search is brand THEN verify use case and navigation are called`() = runTest {
        val recentSearch = RecentSearch.Brand(
            searchTerm = "Brand",
            slug = "brand"
        )
        val screen = Screen.ProductList(productListNavArgs(ProductListType.Brand.Slug(recentSearch.slug)))
        val event = SearchEvent.OnRecentSearchClick(recentSearch)

        viewModel.handleEvent(event)

        viewModel.run {
            verify { navigateTo(screen = screen) }
        }
        coVerify { saveRecentSearch(recentSearch) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnClearRecentSearches THEN verify use case is called`() = runTest {
        coJustRun { clearRecentSearches() }
        val event = SearchEvent.OnClearRecentSearches

        viewModel.handleEvent(event)

        coVerify { clearRecentSearches() }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnDeleteRecentSearch THEN verify use case is called`() = runTest {
        coJustRun { deleteRecentSearch(any()) }
        val recentSearch = RecentSearch.Query(searchTerm = "Search")
        val event = SearchEvent.OnDeleteRecentSearch(recentSearch)

        viewModel.handleEvent(event)

        coVerify { deleteRecentSearch(recentSearch) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnOpenSearchScreen THEN state should be reset`() = runTest {
        val event = SearchEvent.OnOpenSearchScreen

        viewModel.state.test {
            assertEquals(SearchUIState.Empty, awaitItem())

            // Use another event to change state to something different
            viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "Boot"))
            assertEquals(SearchUIState.Loading, awaitItem())

            viewModel.handleEvent(event)
            assertEquals(SearchUIState.Empty, awaitItem())
        }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnKeywordSuggestionClick THEN saves recent search and navigates`() = runTest {
        val keyword = "Search"
        val keywordSuggestion = KeywordSuggestionUI(value = keyword)
        val screen = Screen.ProductList(productListNavArgs(ProductListType.Search(keyword)))
        val event = SearchEvent.OnKeywordSuggestionClick(keywordSuggestion)

        viewModel.handleEvent(event)

        viewModel.run {
            verify { navigateTo(screen = screen) }
        }
        coVerify { saveRecentSearch(RecentSearch.Query(searchTerm = keyword)) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnBrandSuggestionClick THEN saves recent search and navigates`() = runTest {
        val brandSuggestion = BrandSuggestionUI(
            name = "Brand",
            slug = "brand"
        )
        val recentSearch = RecentSearch.Brand(
            searchTerm = brandSuggestion.name,
            slug = brandSuggestion.slug
        )
        val screen = Screen.ProductList(productListNavArgs(ProductListType.Brand.Slug(brandSuggestion.slug)))
        val event = SearchEvent.OnBrandSuggestionClick(brandSuggestion)

        viewModel.handleEvent(event)

        viewModel.run {
            verify { navigateTo(screen = screen) }
        }
        coVerify { saveRecentSearch(recentSearch) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnProductSuggestionClick THEN navigates`() = runTest {
        val productSuggestion = searchUI.products[0]
        val screen = Screen.ProductDetails(productDetailsNavArgs(id = productSuggestion.id))
        val event = SearchEvent.OnProductSuggestionClick(productSuggestion)

        viewModel.handleEvent(event)

        viewModel.run {
            verify { navigateTo(screen = screen) }
        }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnMoreProductsClick WHEN state is loaded THEN saves recent search and navigates`() = runTest {
        coEvery { getSearchSuggestions(any()) } returns UseCaseResult.Success(searchSuggestions)
        coEvery { searchUIFactory(any(), any()) } returns searchUI
        val loaded = SearchUIState.Loaded(searchUI)
        val screen = Screen.ProductList(productListNavArgs(ProductListType.Search(loaded.searchUI.searchTerm)))
        val updateSearchTermEvent = SearchEvent.OnUpdateSearchTerm(searchTerm = loaded.searchUI.searchTerm)
        val event = SearchEvent.OnMoreProductsClick

        viewModel.state.test {
            assertEquals(SearchUIState.Empty, awaitItem())

            viewModel.handleEvent(updateSearchTermEvent)

            assertEquals(SearchUIState.Loading, awaitItem())

            assertEquals(loaded, awaitItem())

            viewModel.handleEvent(event)

            viewModel.run {
                verify { navigateTo(screen = screen) }
            }
            coVerify { saveRecentSearch(RecentSearch.Query(searchTerm = loaded.searchUI.searchTerm)) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnMoreProductsClick WHEN state is not loaded THEN does nothing`() = runTest {
        coEvery { getSearchSuggestions(any()) } returns UseCaseResult.Error(mockk())
        val updateSearchTermEvent = SearchEvent.OnUpdateSearchTerm(searchTerm = "Boot")
        val event = SearchEvent.OnMoreProductsClick

        viewModel.state.test {
            assertEquals(SearchUIState.Empty, awaitItem())

            viewModel.handleEvent(updateSearchTermEvent)

            assertEquals(SearchUIState.Loading, awaitItem())

            assertEquals(SearchUIState.Error("Boot"), awaitItem())

            viewModel.handleEvent(event)

            viewModel.run {
                verify(exactly = 0) { navigateTo(screen = any()) }
            }
            coVerify(exactly = 0) { saveRecentSearch(any()) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnViewAllBrandsClick THEN navigates`() = runTest {
        val screen = Screen.Shop(shopNavArgs(ShopTab.Brands))
        val event = SearchEvent.OnViewAllBrandsClick

        viewModel.handleEvent(event)

        viewModel.run {
            verify {
                navigateClearingStack(
                    screen = screen,
                    saveState = true,
                    restoreState = false,
                    launchSingleTop = true
                )
            }
        }
    }
}
