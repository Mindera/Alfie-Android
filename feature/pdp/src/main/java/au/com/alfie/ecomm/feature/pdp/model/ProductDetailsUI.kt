package au.com.alfie.ecomm.feature.pdp.model

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.core.ui.media.GalleryUI
import au.com.alfie.ecomm.repository.product.model.Variant
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
    val selectedColorUI: ColorUI? = null
)
