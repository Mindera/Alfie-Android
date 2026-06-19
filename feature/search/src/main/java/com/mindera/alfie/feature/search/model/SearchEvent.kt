package com.mindera.alfie.feature.search.model

import com.mindera.alfie.repository.search.model.RecentSearch

internal sealed interface SearchEvent {

    data class OnSearchAction(val searchTerm: String) : SearchEvent

    data class OnRecentSearchClick(val recentSearch: RecentSearch) : SearchEvent

    data class OnDeleteRecentSearch(val recentSearch: RecentSearch) : SearchEvent

    data object OnClearRecentSearches : SearchEvent
}
