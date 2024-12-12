package au.com.alfie.ecomm.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.productDetailsNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.productlist.ProductListType
import au.com.alfie.ecomm.core.navigation.arguments.productlist.productListNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.shop.ShopTab
import au.com.alfie.ecomm.core.navigation.arguments.shop.shopNavArgs
import au.com.alfie.ecomm.domain.doOnResult
import au.com.alfie.ecomm.domain.usecase.search.ClearRecentSearchesUseCase
import au.com.alfie.ecomm.domain.usecase.search.DeleteRecentSearchUseCase
import au.com.alfie.ecomm.domain.usecase.search.GetRecentSearchesUseCase
import au.com.alfie.ecomm.domain.usecase.search.GetSearchSuggestionsUseCase
import au.com.alfie.ecomm.domain.usecase.search.SaveRecentSearchUseCase
import au.com.alfie.ecomm.feature.search.factory.SearchUIFactory
import au.com.alfie.ecomm.feature.search.model.BrandSuggestionUI
import au.com.alfie.ecomm.feature.search.model.KeywordSuggestionUI
import au.com.alfie.ecomm.feature.search.model.ProductSuggestionUI
import au.com.alfie.ecomm.feature.search.model.SearchEvent
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnBrandSuggestionClick
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnClearRecentSearches
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnDeleteRecentSearch
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnKeywordSuggestionClick
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnMoreProductsClick
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnOpenSearchScreen
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnProductSuggestionClick
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnRecentSearchClick
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnSearchAction
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnUpdateSearchTerm
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnViewAllBrandsClick
import au.com.alfie.ecomm.feature.search.model.SearchUIState
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import au.com.alfie.ecomm.repository.search.model.RecentSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    getRecentSearches: GetRecentSearchesUseCase,
    private val saveRecentSearch: SaveRecentSearchUseCase,
    private val deleteRecentSearch: DeleteRecentSearchUseCase,
    private val clearRecentSearches: ClearRecentSearchesUseCase,
    private val getSearchSuggestions: GetSearchSuggestionsUseCase,
    private val searchUIFactory: SearchUIFactory,
    private val uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate {

    companion object {
        private const val TYPING_DEBOUNCE_TIME = 500L
    }

    private val _state: MutableStateFlow<SearchUIState> = MutableStateFlow(SearchUIState.Empty)
    val state = _state.asStateFlow()

    private var job: Job? = null

    val recentSearches = getRecentSearches()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun handleEvent(event: SearchEvent) {
        when (event) {
            is OnUpdateSearchTerm -> handleUpdateSearchTerm(searchTerm = event.searchTerm)
            is OnSearchAction -> handleOnSearchAction(searchTerm = event.searchTerm)
            is OnRecentSearchClick -> handleRecentSearchClick(recentSearch = event.recentSearch)
            is OnClearRecentSearches -> handleClearRecentSearches()
            is OnDeleteRecentSearch -> handleDeleteRecentSearch(recentSearch = event.recentSearch)
            is OnOpenSearchScreen -> handleResetSearchState()
            is OnKeywordSuggestionClick -> handleKeywordSuggestionClick(suggestion = event.keywordSuggestion)
            is OnBrandSuggestionClick -> handleBrandSuggestionClick(suggestion = event.brandSuggestion)
            is OnProductSuggestionClick -> handleProductSuggestionClick(suggestion = event.productSuggestion)
            is OnMoreProductsClick -> handleMoreProductsClick()
            is OnViewAllBrandsClick -> handleViewAllBrandsClick()
        }
    }

    private fun handleOnSearchAction(searchTerm: String) {
        if (searchTerm.trim().isNotEmpty()) {
            navigateToQuery(searchTerm = searchTerm)
        }
    }

    private fun handleRecentSearchClick(recentSearch: RecentSearch) {
        when (recentSearch) {
            is RecentSearch.Query -> navigateToQuery(searchTerm = recentSearch.searchTerm)
            is RecentSearch.Brand -> navigateToBrand(
                name = recentSearch.searchTerm,
                slug = recentSearch.slug
            )
        }
    }

    private fun handleClearRecentSearches() {
        viewModelScope.launch {
            clearRecentSearches()
        }
    }

    private fun handleDeleteRecentSearch(recentSearch: RecentSearch) {
        viewModelScope.launch {
            deleteRecentSearch(recentSearch = recentSearch)
        }
    }

    private fun handleUpdateSearchTerm(searchTerm: String) {
        job?.cancel()
        job = viewModelScope.launch {
            if (searchTerm.trim().isEmpty()) {
                _state.value = SearchUIState.Empty
                return@launch
            }

            if (state.value !is SearchUIState.Loaded) {
                _state.value = SearchUIState.Loading
            }

            delay(TYPING_DEBOUNCE_TIME)

            getSearchSuggestions(query = searchTerm).doOnResult(
                onSuccess = { suggestions ->
                    if (suggestions.isEmpty()) {
                        _state.value = SearchUIState.Error(searchTerm)
                    } else {
                        val searchUI = searchUIFactory(
                            searchTerm = searchTerm,
                            searchSuggestions = suggestions
                        )
                        _state.value = SearchUIState.Loaded(searchUI)
                    }
                },
                onError = {
                    _state.value = SearchUIState.Error(searchTerm)
                }
            )
        }
    }

    private fun handleResetSearchState() {
        _state.value = SearchUIState.Empty
    }

    private fun handleKeywordSuggestionClick(suggestion: KeywordSuggestionUI) {
        navigateToQuery(searchTerm = suggestion.value)
    }

    private fun handleBrandSuggestionClick(suggestion: BrandSuggestionUI) {
        navigateToBrand(
            name = suggestion.name,
            slug = suggestion.slug
        )
    }

    private fun handleProductSuggestionClick(suggestion: ProductSuggestionUI) {
        val navArgs = productDetailsNavArgs(id = suggestion.id)
        val screen = Screen.ProductDetails(navArgs)
        navigateTo(screen = screen)
    }

    private fun handleMoreProductsClick() {
        (state.value as? SearchUIState.Loaded)?.let { loadedState ->
            navigateToQuery(searchTerm = loadedState.searchUI.searchTerm)
        }
    }

    private fun handleViewAllBrandsClick() {
        val screen = Screen.Shop(shopNavArgs(ShopTab.Brands))
        navigateClearingStack(
            screen = screen,
            saveState = true,
            restoreState = false,
            launchSingleTop = true
        )
    }

    private fun navigateToQuery(searchTerm: String) {
        val navArgs = productListNavArgs(ProductListType.Search(query = searchTerm))
        val screen = Screen.ProductList(navArgs)
        navigateTo(screen = screen)
        RecentSearch.Query(searchTerm = searchTerm).save()
    }

    private fun navigateToBrand(
        name: String,
        slug: String
    ) {
        val navArgs = productListNavArgs(ProductListType.Brand.Slug(slug = slug))
        val screen = Screen.ProductList(navArgs)
        navigateTo(screen = screen)
        RecentSearch.Brand(
            searchTerm = name,
            slug = slug
        ).save()
    }

    private fun RecentSearch.save() {
        viewModelScope.launch {
            saveRecentSearch(recentSearch = this@save)
        }
    }
}
