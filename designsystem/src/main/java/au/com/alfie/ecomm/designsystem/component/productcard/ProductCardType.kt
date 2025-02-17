package au.com.alfie.ecomm.designsystem.component.productcard

import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.core.ui.test.PRODUCT_CARD
import au.com.alfie.ecomm.core.ui.test.PRODUCT_COLOR
import au.com.alfie.ecomm.core.ui.test.PRODUCT_DESIGNER
import au.com.alfie.ecomm.core.ui.test.PRODUCT_IMAGE
import au.com.alfie.ecomm.core.ui.test.PRODUCT_NAME
import au.com.alfie.ecomm.core.ui.test.PRODUCT_PRICE_COMPONENT
import au.com.alfie.ecomm.core.ui.test.PRODUCT_SIZE
import au.com.alfie.ecomm.designsystem.component.price.PriceType

sealed interface ProductCardType {

    val image: ImageUI
    val brand: String
    val name: String
    val price: PriceType?
    val cardTestTag: String
    val imageTestTag: String
    val brandTestTag: String
    val nameTestTag: String
    val priceTestTag: String

    data class XSmall(
        override val image: ImageUI,
        override val brand: String,
        override val name: String,
        override val price: PriceType,
        val onRemoveClick: ClickEvent? = null,
        val color: String,
        val size: String,
        override val cardTestTag: String = PRODUCT_CARD,
        override val imageTestTag: String = PRODUCT_IMAGE,
        override val brandTestTag: String = PRODUCT_DESIGNER,
        override val nameTestTag: String = PRODUCT_NAME,
        override val priceTestTag: String = PRODUCT_PRICE_COMPONENT,
        val colorTestTag: String = PRODUCT_COLOR,
        val sizeTestTag: String = PRODUCT_SIZE
    ) : ProductCardType

    data class Small(
        override val image: ImageUI,
        override val brand: String,
        override val name: String,
        override val price: PriceType?,
        override val cardTestTag: String = PRODUCT_CARD,
        override val imageTestTag: String = PRODUCT_IMAGE,
        override val brandTestTag: String = PRODUCT_DESIGNER,
        override val nameTestTag: String = PRODUCT_NAME,
        override val priceTestTag: String = PRODUCT_PRICE_COMPONENT
    ) : ProductCardType

    data class Medium(
        override val image: ImageUI,
        override val brand: String,
        override val name: String,
        override val price: PriceType,
        val onFavoriteClick: ClickEvent,
        override val cardTestTag: String = PRODUCT_CARD,
        override val imageTestTag: String = PRODUCT_IMAGE,
        override val brandTestTag: String = PRODUCT_DESIGNER,
        override val nameTestTag: String = PRODUCT_NAME,
        override val priceTestTag: String = PRODUCT_PRICE_COMPONENT
    ) : ProductCardType

    data class Large(
        override val image: ImageUI,
        override val brand: String,
        override val name: String,
        override val price: PriceType,
        val onFavoriteClick: ClickEvent,
        override val cardTestTag: String = PRODUCT_CARD,
        override val imageTestTag: String = PRODUCT_IMAGE,
        override val brandTestTag: String = PRODUCT_DESIGNER,
        override val nameTestTag: String = PRODUCT_NAME,
        override val priceTestTag: String = PRODUCT_PRICE_COMPONENT
    ) : ProductCardType
}
