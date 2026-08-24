package com.mindera.alfie.feature.pdp.model

import androidx.compose.runtime.Stable
import com.mindera.alfie.core.ui.media.GalleryUI
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.repository.product.model.Variant
import com.mindera.alfie.repository.product.model.colorOptionValue
import com.mindera.alfie.repository.product.model.sizeOptionValue
import kotlinx.collections.immutable.ImmutableList

@Stable
internal data class ProductDetailsUI(
    val id: String,
    val brand: String,
    val name: String,
    val slug: String,
    val description: String,
    val price: PriceType,
    val colors: ImmutableList<ColorUI>,
    val variants: ImmutableList<Variant>,
    val isSelectionSoldOut: Boolean,
    val sections: ImmutableList<ProductDetailsSectionItem>,
    val shareInfo: ProductDetailsShareInfo,
    val gallery: GalleryUI,
    val sizeSectionUI: SizeSectionUI,
    val selectedColorUI: ColorUI? = null,
    val isWishlisted: Boolean = false
) {
    /** Selected colour's name for the description metadata line; null for single-option products. */
    val selectedColourName: String?
        get() = selectedColorUI?.id?.takeIf { it.isNotBlank() }

    /** Reference shoppers quote to customer service — the display variant's SKU. */
    val productReference: String?
        get() = displayVariant()?.sku?.takeIf { it.isNotBlank() }

    /**
     * The variant the price should reflect: the exact colour+size selection when a size is picked,
     * otherwise the first available variant of the selected colour (mirroring the never-blank
     * gallery behaviour on colour change).
     */
    fun displayVariant(): Variant? {
        val byColour = variants.filter { it.colorOptionValue == selectedColorUI?.id }.ifEmpty { variants }
        val selectedSizeId = (sizeSectionUI as? SizeSectionUI.SizeSelector)?.selectedSize?.id
        return selectedSizeId
            ?.let { id -> byColour.firstOrNull { it.sizeOptionValue == id && it.available } }
            ?: selectedSizeId
                ?.let { id -> byColour.firstOrNull { it.sizeOptionValue == id } }
            ?: byColour.firstOrNull { it.available }
            ?: byColour.firstOrNull()
            ?: variants.firstOrNull()
    }
}
