package com.mindera.alfie.feature.mappers

import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.repository.productlist.model.ProductListEntry

/**
 * The single mapping from a product list entry to the vertical product card, shared by every
 * surface that shows a product grid — the PLP and the PDP's recommendations — so they cannot
 * drift apart.
 */
fun ProductListEntry.toVerticalProductCard(
    onProductClick: ClickEvent,
    onFavoriteClick: ClickEvent
): ProductCardType.Vertical = ProductCardType.Vertical(
    brand = brandName.orEmpty(),
    name = name,
    price = priceRange.toPriceType(),
    image = primaryImage.toImageUI(),
    onClick = onProductClick,
    onFavoriteClick = onFavoriteClick,
    label = tags.firstOrNull()
)
