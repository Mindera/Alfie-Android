package com.mindera.alfie.feature.wishlist.models

import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.component.productcard.ProductCardType

data class WishlistProductUi(
    val productCardData: ProductCardType,
    val onClick: ClickEvent
)
