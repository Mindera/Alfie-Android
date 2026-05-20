package com.mindera.alfie.feature.search.model

import com.mindera.alfie.repository.search.model.RecentSearch

internal sealed interface SearchEvent {

    data class OnUpdateSearchTerm(val searchTerm: String) : SearchEvent

    data class OnSearchAction(val searchTerm: String) : SearchEvent

    data class OnRecentSearchClick(val recentSearch: RecentSearch) : SearchEvent

    data class OnDeleteRecentSearch(val recentSearch: RecentSearch) : SearchEvent

    data object OnClearRecentSearches : SearchEvent

    data object OnOpenSearchScreen : SearchEvent

    data class OnKeywordSuggestionClick(val keywordSuggestion: KeywordSuggestionUI) : SearchEvent

    data class OnBrandSuggestionClick(val brandSuggestion: BrandSuggestionUI) : SearchEvent

    data object OnMoreProductsClick : SearchEvent

    data object OnViewAllBrandsClick : SearchEvent
}
