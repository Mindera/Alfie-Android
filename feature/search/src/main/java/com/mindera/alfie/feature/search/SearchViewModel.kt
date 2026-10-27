package com.mindera.alfie.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.productlist.ProductListType
import com.mindera.alfie.core.navigation.arguments.productlist.productListNavArgs
import com.mindera.alfie.domain.doOnResult
import com.mindera.alfie.domain.usecase.search.ClearRecentSearchesUseCase
import com.mindera.alfie.domain.usecase.search.DeleteRecentSearchUseCase
import com.mindera.alfie.domain.usecase.search.GetRecentSearchesUseCase
import com.mindera.alfie.domain.usecase.search.GetSearchSuggestionsUseCase
import com.mindera.alfie.domain.usecase.search.SaveRecentSearchUseCase
import com.mindera.alfie.feature.search.factory.SearchUIFactory
import com.mindera.alfie.feature.search.model.BrandSuggestionUI
import com.mindera.alfie.feature.search.model.SearchEvent
import com.mindera.alfie.feature.search.model.SearchEvent.OnBrandSuggestionClick
import com.mindera.alfie.feature.search.model.SearchEvent.OnClearRecentSearches
import com.mindera.alfie.feature.search.model.SearchEvent.OnDeleteRecentSearch
import com.mindera.alfie.feature.search.model.SearchEvent.OnKeywordSuggestionClick
import com.mindera.alfie.feature.search.model.SearchEvent.OnMoreProductsClick
import com.mindera.alfie.feature.search.model.SearchEvent.OnRecentSearchClick
import com.mindera.alfie.feature.search.model.SearchEvent.OnRetrySuggestions
import com.mindera.alfie.feature.search.model.SearchEvent.OnSearchAction
import com.mindera.alfie.feature.search.model.SearchEvent.OnUpdateSearchTerm
import com.mindera.alfie.feature.search.model.SearchUIState
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.search.model.RecentSearch
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
    private val getSearchSuggestions: GetSearchSuggestionsUseCase,
    private val searchUIFactory: SearchUIFactory,
    private val saveRecentSearch: SaveRecentSearchUseCase,
    private val deleteRecentSearch: DeleteRecentSearchUseCase,
    private val clearRecentSearches: ClearRecentSearchesUseCase,
    private val uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate {

    companion object {
        private const val TYPING_DEBOUNCE_TIME = 500L

        // Per suggestion type: the BFF returns up to this many keywords, brands and products each.
        private const val SUGGESTION_LIMIT = 6
    }

    private val _state: MutableStateFlow<SearchUIState> = MutableStateFlow(SearchUIState.Empty)
    val state = _state.asStateFlow()

    val recentSearches = getRecentSearches()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private var suggestionsJob: Job? = null
    private var searchTerm: String = ""

    fun handleEvent(event: SearchEvent) {
        when (event) {
            is OnUpdateSearchTerm -> handleUpdateSearchTerm(searchTerm = event.searchTerm)
            is OnSearchAction -> handleOnSearchAction(searchTerm = event.searchTerm)
            is OnRecentSearchClick -> handleRecentSearchClick(recentSearch = event.recentSearch)
            is OnClearRecentSearches -> handleClearRecentSearches()
            is OnDeleteRecentSearch -> handleDeleteRecentSearch(recentSearch = event.recentSearch)
            is OnKeywordSuggestionClick -> navigateToQuery(searchTerm = event.keywordSuggestion.value)
            is OnBrandSuggestionClick -> handleBrandSuggestionClick(suggestion = event.brandSuggestion)
            is OnMoreProductsClick -> handleMoreProductsClick()
            is OnRetrySuggestions -> loadSuggestions(debounce = false)
        }
    }

    private fun handleUpdateSearchTerm(searchTerm: String) {
        val trimmedTerm = searchTerm.trim()
        if (trimmedTerm == this.searchTerm) return
        this.searchTerm = trimmedTerm
        loadSuggestions(debounce = true)
    }

    /**
     * Cancelling the previous job on every call is what keeps a slow response for an older term
     * from overwriting the suggestions for what is typed now.
     */
    private fun loadSuggestions(debounce: Boolean) {
        suggestionsJob?.cancel()
        val term = searchTerm
        if (term.isEmpty()) {
            _state.value = SearchUIState.Empty
            return
        }

        suggestionsJob = viewModelScope.launch {
            // Keeps the current suggestions on screen while typing on, instead of flashing a loader.
            if (state.value !is SearchUIState.Loaded) {
                _state.value = SearchUIState.Loading
            }
            if (debounce) delay(TYPING_DEBOUNCE_TIME)

            getSearchSuggestions(query = term, limit = SUGGESTION_LIMIT).doOnResult(
                onSuccess = { suggestions ->
                    _state.value = if (suggestions.isEmpty()) {
                        SearchUIState.NoResults(searchTerm = term)
                    } else {
                        SearchUIState.Loaded(
                            searchUIFactory(
                                searchTerm = term,
                                searchSuggestions = suggestions,
                                onProductClick = ::navigateToProduct
                            )
                        )
                    }
                },
                onError = {
                    _state.value = SearchUIState.Error(searchTerm = term)
                }
            )
        }
    }

    private fun handleOnSearchAction(searchTerm: String) {
        val trimmedTerm = searchTerm.trim()
        if (trimmedTerm.isNotEmpty()) {
            navigateToQuery(searchTerm = trimmedTerm)
        }
    }

    private fun handleRecentSearchClick(recentSearch: RecentSearch) {
        when (recentSearch) {
            is RecentSearch.Query -> navigateToQuery(searchTerm = recentSearch.searchTerm)
            is RecentSearch.Brand -> navigateToBrand(
                name = recentSearch.searchTerm,
                key = recentSearch.key
            )
        }
    }

    private fun handleBrandSuggestionClick(suggestion: BrandSuggestionUI) {
        navigateToBrand(
            name = suggestion.name,
            key = suggestion.key
        )
    }

    private fun handleMoreProductsClick() {
        (state.value as? SearchUIState.Loaded)?.let { loadedState ->
            navigateToQuery(searchTerm = loadedState.searchUI.searchTerm)
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

    private fun navigateToQuery(searchTerm: String) {
        val navArgs = productListNavArgs(ProductListType.Search(query = searchTerm))
        val screen = Screen.ProductList(navArgs)
        navigateTo(screen = screen)
        RecentSearch.Query(searchTerm = searchTerm).save()
    }

    private fun navigateToBrand(
        name: String,
        key: String
    ) {
        val navArgs = productListNavArgs(
            ProductListType.Brand.Key(
                key = key,
                name = name
            )
        )
        val screen = Screen.ProductList(navArgs)
        navigateTo(screen = screen)
        RecentSearch.Brand(
            searchTerm = name,
            key = key
        ).save()
    }

    private fun navigateToProduct(slug: String) {
        navigateTo(screen = Screen.ProductDetails(productDetailsNavArgs(handle = slug)))
    }

    private fun RecentSearch.save() {
        viewModelScope.launch {
            saveRecentSearch(recentSearch = this@save)
        }
    }
}
