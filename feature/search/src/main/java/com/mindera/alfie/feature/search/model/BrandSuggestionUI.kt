package com.mindera.alfie.feature.search.model

/**
 * @param key opaque BFF brand key, passed back to list the brand's products.
 */
internal data class BrandSuggestionUI(
    val key: String,
    val name: String
)
