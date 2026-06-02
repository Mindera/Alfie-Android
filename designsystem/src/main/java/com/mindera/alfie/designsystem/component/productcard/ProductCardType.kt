package com.mindera.alfie.designsystem.component.productcard

import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.core.ui.test.PRODUCT_CARD
import com.mindera.alfie.core.ui.test.PRODUCT_COLOR
import com.mindera.alfie.core.ui.test.PRODUCT_DESIGNER
import com.mindera.alfie.core.ui.test.PRODUCT_IMAGE
import com.mindera.alfie.core.ui.test.PRODUCT_NAME
import com.mindera.alfie.core.ui.test.PRODUCT_PRICE_COMPONENT
import com.mindera.alfie.core.ui.test.PRODUCT_SIZE
import com.mindera.alfie.designsystem.component.price.PriceType

sealed interface ProductCardType {

    val image: ImageUI
    val brand: String
    val name: String
    val price: PriceType
    val cardTestTag: String
    val imageTestTag: String
    val brandTestTag: String
    val nameTestTag: String
    val priceTestTag: String
    val onClick: ClickEvent?
    val onRemoveClick: ClickEvent?

    data class Horizontal(
        override val image: ImageUI,
        override val brand: String,
        override val name: String,
        override val price: PriceType,
        val color: String,
        val size: String,
        override val onClick: ClickEvent? = null,
        override val onRemoveClick: ClickEvent? = null,
        override val cardTestTag: String = PRODUCT_CARD,
        override val imageTestTag: String = PRODUCT_IMAGE,
        override val brandTestTag: String = PRODUCT_DESIGNER,
        override val nameTestTag: String = PRODUCT_NAME,
        override val priceTestTag: String = PRODUCT_PRICE_COMPONENT,
        val colorTestTag: String = PRODUCT_COLOR,
        val sizeTestTag: String = PRODUCT_SIZE
    ) : ProductCardType

    data class Vertical(
        override val image: ImageUI,
        override val brand: String,
        override val name: String,
        override val price: PriceType,
        override val onClick: ClickEvent? = null,
        val onFavoriteClick: ClickEvent? = null,
        override val onRemoveClick: ClickEvent? = null,
        val label: String? = null,
        override val cardTestTag: String = PRODUCT_CARD,
        override val imageTestTag: String = PRODUCT_IMAGE,
        override val brandTestTag: String = PRODUCT_DESIGNER,
        override val nameTestTag: String = PRODUCT_NAME,
        override val priceTestTag: String = PRODUCT_PRICE_COMPONENT,
        val addToBagClick: ClickEvent? = null
    ) : ProductCardType
}
