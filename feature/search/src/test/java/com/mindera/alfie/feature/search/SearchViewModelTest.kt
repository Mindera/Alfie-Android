package com.mindera.alfie.feature.search

import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.productlist.ProductListType
import com.mindera.alfie.core.navigation.arguments.productlist.productListNavArgs
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.search.ClearRecentSearchesUseCase
import com.mindera.alfie.domain.usecase.search.DeleteRecentSearchUseCase
import com.mindera.alfie.domain.usecase.search.GetRecentSearchesUseCase
import com.mindera.alfie.domain.usecase.search.GetSearchSuggestionsUseCase
import com.mindera.alfie.domain.usecase.search.SaveRecentSearchUseCase
import com.mindera.alfie.feature.search.factory.SearchUIFactory
import com.mindera.alfie.feature.search.model.BrandSuggestionUI
import com.mindera.alfie.feature.search.model.KeywordSuggestionUI
import com.mindera.alfie.feature.search.model.SearchEvent
import com.mindera.alfie.feature.search.model.SearchUIState
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.search.model.RecentSearch
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class SearchViewModelTest {

    @RelaxedMockK
    private lateinit var getRecentSearches: GetRecentSearchesUseCase

    @RelaxedMockK
    private lateinit var getSearchSuggestions: GetSearchSuggestionsUseCase

    @RelaxedMockK
    private lateinit var searchUIFactory: SearchUIFactory

    @RelaxedMockK
    private lateinit var saveRecentSearch: SaveRecentSearchUseCase

    @RelaxedMockK
    private lateinit var deleteRecentSearch: DeleteRecentSearchUseCase

    @RelaxedMockK
    private lateinit var clearRecentSearches: ClearRecentSearchesUseCase

    @RelaxedMockK
    private lateinit var uiEventEmitterDelegate: UIEventEmitterDelegate

    @InjectMockKs
    private lateinit var viewModel: SearchViewModel

    @BeforeEach
    fun setup() {
        coEvery { getRecentSearches() } returns mockk<Flow<List<RecentSearch>>>()
    }

    @Test
    fun `WHEN handleEvent GIVEN OnSearchAction THEN saves recent search and navigates to search PLP`() = runTest {
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
    fun `WHEN handleEvent GIVEN OnSearchAction WHEN term is blank THEN does nothing`() = runTest {
        val event = SearchEvent.OnSearchAction(searchTerm = "   ")

        viewModel.handleEvent(event)

        viewModel.run {
            verify(exactly = 0) { navigateTo(screen = any()) }
        }
        coVerify(exactly = 0) { saveRecentSearch(any()) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnRecentSearchClick WHEN recent search is query THEN saves recent search and navigates`() = runTest {
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
    fun `WHEN handleEvent GIVEN OnRecentSearchClick WHEN recent search is brand THEN saves recent search and navigates`() = runTest {
        val recentSearch = RecentSearch.Brand(
            searchTerm = "Brand",
            key = "brand"
        )
        val screen = Screen.ProductList(productListNavArgs(ProductListType.Brand.Key(key = recentSearch.key, name = recentSearch.searchTerm)))
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
    fun `GIVEN screen start THEN state is Empty`() {
        assertEquals(SearchUIState.Empty, viewModel.state.value)
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm WHEN term is blank THEN state is Empty and nothing is fetched`() = runTest {
        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "   "))
        advanceUntilIdle()

        assertEquals(SearchUIState.Empty, viewModel.state.value)
        coVerify(exactly = 0) { getSearchSuggestions(any(), any()) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm THEN shows Loading and fetches trimmed term after the debounce`() = runTest {
        coEvery { getSearchSuggestions(any(), any()) } returns UseCaseResult.Success(searchSuggestions)
        coEvery { searchUIFactory(any(), any(), any()) } returns searchUI()

        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = " shirt "))
        assertEquals(SearchUIState.Loading, viewModel.state.value)

        advanceTimeBy(499)
        coVerify(exactly = 0) { getSearchSuggestions(any(), any()) }

        advanceUntilIdle()
        coVerify { getSearchSuggestions(query = "shirt", limit = 6) }
        assertEquals(SearchUIState.Loaded(searchUI()), viewModel.state.value)
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm typed in quick succession THEN fetches only the last term`() = runTest {
        coEvery { getSearchSuggestions(any(), any()) } returns UseCaseResult.Success(searchSuggestions)

        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "sh"))
        advanceTimeBy(300)
        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "shi"))
        advanceUntilIdle()

        coVerify(exactly = 0) { getSearchSuggestions(query = "sh", limit = any()) }
        coVerify(exactly = 1) { getSearchSuggestions(query = "shi", limit = any()) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm WHEN an older response arrives late THEN it never replaces the newer one`() = runTest {
        coEvery { getSearchSuggestions(query = "sh", limit = any()) } coAnswers {
            delay(2_000)
            UseCaseResult.Success(searchSuggestions)
        }
        coEvery { getSearchSuggestions(query = "shi", limit = any()) } returns UseCaseResult.Success(searchSuggestions)
        coEvery { searchUIFactory(any(), any(), any()) } answers { searchUI(searchTerm = firstArg()) }

        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "sh"))
        advanceTimeBy(600) // "sh" is now in flight
        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "shi"))
        advanceUntilIdle()

        assertEquals(SearchUIState.Loaded(searchUI(searchTerm = "shi")), viewModel.state.value)
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm WHEN suggestions are empty THEN state is NoResults`() = runTest {
        coEvery { getSearchSuggestions(any(), any()) } returns UseCaseResult.Success(emptySearchSuggestions)

        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "zzz"))
        advanceUntilIdle()

        assertEquals(SearchUIState.NoResults(searchTerm = "zzz"), viewModel.state.value)
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm WHEN the request fails THEN state is Error`() = runTest {
        coEvery { getSearchSuggestions(any(), any()) } returns UseCaseResult.Error(mockk())

        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "shirt"))
        advanceUntilIdle()

        assertEquals(SearchUIState.Error(searchTerm = "shirt"), viewModel.state.value)
    }

    @Test
    fun `WHEN handleEvent GIVEN OnRetrySuggestions THEN refetches the current term`() = runTest {
        coEvery { getSearchSuggestions(any(), any()) } returns UseCaseResult.Error(mockk())
        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "shirt"))
        advanceUntilIdle()

        coEvery { getSearchSuggestions(any(), any()) } returns UseCaseResult.Success(searchSuggestions)
        coEvery { searchUIFactory(any(), any(), any()) } returns searchUI()
        viewModel.handleEvent(SearchEvent.OnRetrySuggestions)
        advanceUntilIdle()

        coVerify(exactly = 2) { getSearchSuggestions(query = "shirt", limit = any()) }
        assertIs<SearchUIState.Loaded>(viewModel.state.value)
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm cleared THEN returns to Empty`() = runTest {
        coEvery { getSearchSuggestions(any(), any()) } returns UseCaseResult.Success(searchSuggestions)
        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "shirt"))
        advanceUntilIdle()

        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = ""))

        assertEquals(SearchUIState.Empty, viewModel.state.value)
    }

    @Test
    fun `WHEN a product suggestion is clicked THEN navigates to its PDP`() = runTest {
        coEvery { getSearchSuggestions(any(), any()) } returns UseCaseResult.Success(searchSuggestions)
        coEvery { searchUIFactory(any(), any(), any()) } answers {
            thirdArg<(String) -> Unit>().invoke("t-shirt")
            searchUI()
        }

        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "shirt"))
        advanceUntilIdle()

        viewModel.run {
            verify { navigateTo(screen = Screen.ProductDetails(productDetailsNavArgs(handle = "t-shirt"))) }
        }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnKeywordSuggestionClick THEN saves recent search and navigates to search PLP`() = runTest {
        val screen = Screen.ProductList(productListNavArgs(ProductListType.Search("shirt")))

        viewModel.handleEvent(SearchEvent.OnKeywordSuggestionClick(KeywordSuggestionUI(value = "shirt")))

        viewModel.run {
            verify { navigateTo(screen = screen) }
        }
        coVerify { saveRecentSearch(RecentSearch.Query(searchTerm = "shirt")) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnBrandSuggestionClick THEN saves recent brand and navigates to the brand PLP by key`() = runTest {
        val brand = BrandSuggestionUI(key = "935", name = "Kashmere")
        val screen = Screen.ProductList(productListNavArgs(ProductListType.Brand.Key(key = "935", name = "Kashmere")))

        viewModel.handleEvent(SearchEvent.OnBrandSuggestionClick(brand))

        viewModel.run {
            verify { navigateTo(screen = screen) }
        }
        coVerify { saveRecentSearch(RecentSearch.Brand(searchTerm = "Kashmere", key = "935")) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnMoreProductsClick THEN navigates to search PLP for the typed term`() = runTest {
        coEvery { getSearchSuggestions(any(), any()) } returns UseCaseResult.Success(searchSuggestions)
        coEvery { searchUIFactory(any(), any(), any()) } returns searchUI(searchTerm = "sh")
        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "sh"))
        advanceUntilIdle()

        // Typed on, but the debounce hasn't fired yet.
        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "shoes"))
        viewModel.handleEvent(SearchEvent.OnMoreProductsClick)

        viewModel.run {
            verify { navigateTo(screen = Screen.ProductList(productListNavArgs(ProductListType.Search("shoes")))) }
        }
        coVerify { saveRecentSearch(RecentSearch.Query(searchTerm = "shoes")) }
    }

    @Test
    fun `WHEN handleEvent GIVEN OnUpdateSearchTerm while suggestions are shown THEN replaces them with Loading at once`() = runTest {
        coEvery { getSearchSuggestions(any(), any()) } returns UseCaseResult.Success(searchSuggestions)
        coEvery { searchUIFactory(any(), any(), any()) } returns searchUI(searchTerm = "sh")
        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "sh"))
        advanceUntilIdle()
        assertIs<SearchUIState.Loaded>(viewModel.state.value)

        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "shi"))

        assertEquals(SearchUIState.Loading, viewModel.state.value)
    }

    @Test
    fun `WHEN a request for an older term fails after the term changed THEN its error is never shown`() = runTest {
        // Changing the term from inside the call stands in for a superseded request that still
        // completes with an error, e.g. a cancellation caught and returned as a failure.
        coEvery { getSearchSuggestions(query = "shirt", limit = any()) } coAnswers {
            viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "shirts"))
            UseCaseResult.Error(mockk())
        }
        coEvery { getSearchSuggestions(query = "shirts", limit = any()) } returns UseCaseResult.Success(searchSuggestions)
        coEvery { searchUIFactory(any(), any(), any()) } answers { searchUI(searchTerm = firstArg()) }

        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(searchTerm = "shirt"))
        advanceTimeBy(501)
        assertEquals(SearchUIState.Loading, viewModel.state.value)

        advanceUntilIdle()
        assertEquals(SearchUIState.Loaded(searchUI(searchTerm = "shirts")), viewModel.state.value)
    }
}
