package au.com.alfie.ecomm.feature.wishlist.models

import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType

data class WishlistProductUi(
    val productCardData: ProductCardType,
    val onClick: ClickEvent
)
