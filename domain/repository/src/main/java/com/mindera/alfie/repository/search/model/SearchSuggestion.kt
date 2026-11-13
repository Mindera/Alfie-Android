package com.mindera.alfie.repository.search.model

import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money

sealed interface SearchSuggestion {

    data class Keyword(
        val value: String,
        val resultCount: Int?
    ) : SearchSuggestion

    /**
     * @param key opaque, platform-resolved brand key. Pass it back to `filters.brandNames` to list
     * the brand's products; it is not a slug and not URL-safe on every platform.
     */
    data class Brand(
        val key: String,
        val name: String,
        val resultCount: Int?
    ) : SearchSuggestion

    data class Product(
        val id: String,
        val name: String,
        val slug: String,
        val brandName: String?,
        val price: Money?,
        val images: List<Media.Image>
    ) : SearchSuggestion
}
