package com.mindera.alfie.feature.pdp.model

import androidx.compose.runtime.Stable
import com.mindera.alfie.core.ui.media.GalleryUI
import com.mindera.alfie.repository.product.model.Variant
import kotlinx.collections.immutable.ImmutableList

@Stable
internal data class ProductDetailsUI(
    val id: String,
    val brand: String,
    val name: String,
    val slug: String,
    val shortDescription: String,
    val colors: ImmutableList<ColorUI>,
    val information: ImmutableList<InformationUI>,
    val variants: ImmutableList<Variant>,
    val isSelectionSoldOut: Boolean,
    val sections: ImmutableList<ProductDetailsSectionItem>,
    val shareInfo: ProductDetailsShareInfo,
    val gallery: GalleryUI,
    val sizeSectionUI: SizeSectionUI,
    val selectedColorUI: ColorUI? = null,
    val isWishlisted: Boolean = false
)
