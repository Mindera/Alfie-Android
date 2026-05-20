package com.mindera.alfie.repository.search.model

import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.shared.model.Media

sealed interface SearchSuggestion {

    data class Keyword(
        val value: String,
        val resultCount: Int
    ) : SearchSuggestion

    data class Brand(
        val name: String,
        val slug: String,
        val resultCount: Int
    ) : SearchSuggestion

    data class Product(
        val id: String,
        val name: String,
        val slug: String,
        val brandName: String,
        val price: Price,
        val media: List<Media.Image>
    ) : SearchSuggestion
}
