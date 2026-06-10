package com.mindera.alfie.feature.search

import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productlist.ProductListType
import com.mindera.alfie.core.navigation.arguments.productlist.productListNavArgs
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.domain.usecase.search.ClearRecentSearchesUseCase
import com.mindera.alfie.domain.usecase.search.DeleteRecentSearchUseCase
import com.mindera.alfie.domain.usecase.search.GetRecentSearchesUseCase
import com.mindera.alfie.domain.usecase.search.SaveRecentSearchUseCase
import com.mindera.alfie.feature.search.model.SearchEvent
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

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
}
