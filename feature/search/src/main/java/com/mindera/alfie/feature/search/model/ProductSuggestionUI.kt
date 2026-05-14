package com.mindera.alfie.feature.search.model

import com.mindera.alfie.designsystem.component.productcard.ProductCardType

internal data class ProductSuggestionUI(
    val id: String,
    val slug: String,
    val productCardData: ProductCardType.Small
)
