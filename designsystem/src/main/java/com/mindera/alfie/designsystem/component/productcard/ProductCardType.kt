package com.mindera.alfie.designsystem.component.productcard

import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.core.ui.test.PRODUCT_CARD
import com.mindera.alfie.core.ui.test.PRODUCT_COLOR
import com.mindera.alfie.core.ui.test.PRODUCT_DESIGNER
import com.mindera.alfie.core.ui.test.PRODUCT_IMAGE
import com.mindera.alfie.core.ui.test.PRODUCT_NAME
import com.mindera.alfie.core.ui.test.PRODUCT_PRICE_COMPONENT
import com.mindera.alfie.core.ui.test.PRODUCT_QUANTITY
import com.mindera.alfie.core.ui.test.PRODUCT_REFERENCE
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

    /**
     * Row-shaped card: image alongside the product's details, quantity and price.
     *
     * [brand] is carried for the [ProductCardType] contract but is not drawn — this layout leads
     * with the product name.
     *
     * @param reference variant reference, shown prefixed with "Ref.". Blank hides the row.
     * @param quantity how many of this variant the row stands for.
     * @param isAvailable when false the whole row is greyed out.
     * @param onOverflowClick backs the trailing overflow button. Null hides it.
     */
    data class Horizontal(
        override val image: ImageUI,
        override val brand: String,
        override val name: String,
        override val price: PriceType,
        val color: String,
        val size: String,
        val reference: String = "",
        val quantity: Int = 1,
        val isAvailable: Boolean = true,
        val onOverflowClick: ClickEvent? = null,
        override val onClick: ClickEvent? = null,
        override val onRemoveClick: ClickEvent? = null,
        override val cardTestTag: String = PRODUCT_CARD,
        override val imageTestTag: String = PRODUCT_IMAGE,
        override val brandTestTag: String = PRODUCT_DESIGNER,
        override val nameTestTag: String = PRODUCT_NAME,
        override val priceTestTag: String = PRODUCT_PRICE_COMPONENT,
        val colorTestTag: String = PRODUCT_COLOR,
        val sizeTestTag: String = PRODUCT_SIZE,
        val referenceTestTag: String = PRODUCT_REFERENCE,
        val quantityTestTag: String = PRODUCT_QUANTITY
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
