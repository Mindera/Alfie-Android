package com.mindera.alfie.repository.search.model

/**
 * Autocomplete hints for a partial query. Any list may be empty: the BFF returns whatever the
 * configured platform supports (BigCommerce, for one, suggests products only).
 */
data class SearchSuggestions(
    val keywords: List<SearchSuggestion.Keyword>,
    val brands: List<SearchSuggestion.Brand>,
    val products: List<SearchSuggestion.Product>
) {

    fun isEmpty(): Boolean = keywords.isEmpty() && brands.isEmpty() && products.isEmpty()
}
