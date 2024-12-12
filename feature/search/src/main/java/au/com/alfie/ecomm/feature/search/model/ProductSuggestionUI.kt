package au.com.alfie.ecomm.feature.search.model

import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType

internal data class ProductSuggestionUI(
    val id: String,
    val slug: String,
    val productCardData: ProductCardType.Small
)
