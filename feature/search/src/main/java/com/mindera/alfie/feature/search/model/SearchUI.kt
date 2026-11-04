package com.mindera.alfie.feature.search.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class SearchUI(
    val searchTerm: String,
    val keywords: ImmutableList<KeywordSuggestionUI>,
    val brands: ImmutableList<BrandSuggestionUI>,
    val products: ImmutableList<ProductSuggestionUI>
)
