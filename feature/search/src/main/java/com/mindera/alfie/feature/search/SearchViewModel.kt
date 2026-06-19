package com.mindera.alfie.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.productlist.ProductListType
import com.mindera.alfie.core.navigation.arguments.productlist.productListNavArgs
import com.mindera.alfie.domain.usecase.search.ClearRecentSearchesUseCase
import com.mindera.alfie.domain.usecase.search.DeleteRecentSearchUseCase
import com.mindera.alfie.domain.usecase.search.GetRecentSearchesUseCase
import com.mindera.alfie.domain.usecase.search.SaveRecentSearchUseCase
import com.mindera.alfie.feature.search.model.SearchEvent
import com.mindera.alfie.feature.search.model.SearchEvent.OnClearRecentSearches
import com.mindera.alfie.feature.search.model.SearchEvent.OnDeleteRecentSearch
import com.mindera.alfie.feature.search.model.SearchEvent.OnRecentSearchClick
import com.mindera.alfie.feature.search.model.SearchEvent.OnSearchAction
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.search.model.RecentSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    getRecentSearches: GetRecentSearchesUseCase,
    private val saveRecentSearch: SaveRecentSearchUseCase,
    private val deleteRecentSearch: DeleteRecentSearchUseCase,
    private val clearRecentSearches: ClearRecentSearchesUseCase,
    private val uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate {

    val recentSearches = getRecentSearches()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun handleEvent(event: SearchEvent) {
        when (event) {
            is OnSearchAction -> handleOnSearchAction(searchTerm = event.searchTerm)
            is OnRecentSearchClick -> handleRecentSearchClick(recentSearch = event.recentSearch)
            is OnClearRecentSearches -> handleClearRecentSearches()
            is OnDeleteRecentSearch -> handleDeleteRecentSearch(recentSearch = event.recentSearch)
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
