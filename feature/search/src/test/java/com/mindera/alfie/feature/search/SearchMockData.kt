package com.mindera.alfie.feature.search

import com.mindera.alfie.feature.search.model.BrandSuggestionUI
import com.mindera.alfie.feature.search.model.KeywordSuggestionUI
import com.mindera.alfie.feature.search.model.SearchUI
import com.mindera.alfie.repository.search.model.SearchSuggestion
import com.mindera.alfie.repository.search.model.SearchSuggestions
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money
import kotlinx.collections.immutable.persistentListOf

internal val searchSuggestions = SearchSuggestions(
    keywords = listOf(
        SearchSuggestion.Keyword(
            value = "shirt",
            resultCount = 3
        )
    ),
    brands = listOf(
        SearchSuggestion.Brand(
            key = "935",
            name = "Kashmere",
            resultCount = null
        )
    ),
    products = listOf(
        SearchSuggestion.Product(
            id = "12345",
            name = "T-Shirt",
            brandName = "Alfie Basics",
            slug = "t-shirt",
            price = Money(
                amount = 8.0,
                amountFormatted = "£8.00",
                currencyCode = "GBP"
            ),
            images = listOf(
                Media.Image(
                    url = "https://alfie.test/t-shirt.jpg",
                    alt = "T-Shirt"
                )
            )
        )
    )
)

internal val emptySearchSuggestions = SearchSuggestions(
    keywords = emptyList(),
    brands = emptyList(),
    products = emptyList()
)

internal fun searchUI(searchTerm: String = "shirt") = SearchUI(
    searchTerm = searchTerm,
    keywords = persistentListOf(KeywordSuggestionUI(value = "shirt")),
    brands = persistentListOf(
        BrandSuggestionUI(
            key = "935",
            name = "Kashmere"
        )
    ),
    products = persistentListOf()
)
