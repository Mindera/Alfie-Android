package com.mindera.alfie.feature.pdp

import androidx.core.text.HtmlCompat
import com.mindera.alfie.core.commons.color.ColorNameToHex
import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.environment.EnvironmentManager
import com.mindera.alfie.core.environment.model.Environment
import com.mindera.alfie.core.ui.media.GalleryUI
import com.mindera.alfie.core.ui.media.MediaUI
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonProperties
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonState
import com.mindera.alfie.designsystem.component.swatch.SwatchType
import com.mindera.alfie.designsystem.component.tab.TabItem
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.feature.pdp.model.ColorUI
import com.mindera.alfie.feature.pdp.model.InformationUI
import com.mindera.alfie.feature.pdp.model.ProductDetailsSectionItem
import com.mindera.alfie.feature.pdp.model.ProductDetailsShareInfo
import com.mindera.alfie.feature.pdp.model.ProductDetailsUI
import com.mindera.alfie.feature.pdp.model.SizeSectionUI
import com.mindera.alfie.feature.pdp.model.SizeUI
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
import com.mindera.alfie.repository.shared.model.Media
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.compose.ui.graphics.Color as ComposeColor

internal class ProductDetailsUIFactory @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val environmentManager: EnvironmentManager
) {
    companion object {
        private const val SECTION_PLACEHOLDER_COUNT = 2
        private const val SIZE_SELECTOR_THRESHOLD = 6
        internal const val DELIVERY_RETURNS_URL = "return-options"
        internal const val PAYMENT_OPTIONS_URL = "payment-options"

        private const val OPTION_NAME_COLOR = "color"
        private const val OPTION_NAME_COLOUR = "colour"
        private const val OPTION_NAME_SIZE = "size"

        val LOADING = ProductDetailsUI(
            id = "",
            brand = "",
            name = "",
            slug = "",
            colors = persistentListOf(
                ColorUI(
                    id = "",
                    type = SwatchType.PlainColor(ComposeColor.Transparent),
                    index = 0
                )
            ),
            information = persistentListOf(
                InformationUI.Description(
                    TabItem(StringResource.EMPTY),
                    content = ""
                )
            ),
            variants = persistentListOf(),
            isSelectionSoldOut = false,
            sections = List(SECTION_PLACEHOLDER_COUNT) { ProductDetailsSectionItem.EMPTY }.toImmutableList(),
            shareInfo = ProductDetailsShareInfo.EMPTY,
            gallery = GalleryUI.EMPTY,
            sizeSectionUI = SizeSectionUI.NoSize
        )
    }

    suspend operator fun invoke(product: Product): ProductDetailsUI = withContext(dispatcher.default()) {
        val environment = environmentManager.current()
        val defaultVariant = product.resolveDefaultVariant()
        val colors = product.mapColors()
        val selectedColor = colors.findSelected(defaultVariant)
        val descriptionItem = InformationUI.Description(
            tabItem = TabItem(StringResource.fromId(R.string.product_details_information_description)),
            content = product.descriptionHtml.stripHtml()
        )
        ProductDetailsUI(
            id = product.id,
            brand = product.brandName.orEmpty(),
            name = product.name,
            slug = product.slug,
            information = persistentListOf(descriptionItem),
            variants = product.variants.toImmutableList(),
            colors = colors.toImmutableList(),
            selectedColorUI = selectedColor,
            isSelectionSoldOut = product.variants.isSoldOut(selectedColor?.id),
            sections = getSectionsList(environment = environment),
            shareInfo = buildProductDetailsShareInfo(
                brand = product.brandName.orEmpty(),
                name = product.name,
                slug = product.slug,
                price = defaultVariant?.price?.amount?.amountFormatted.orEmpty()
            ),
            gallery = product.galleryFor(selectedColor?.id, defaultVariant),
            sizeSectionUI = product.variants.toSizeSectionUI(selectedColor)
        )
    }

    suspend fun setSelectedColour(
        details: ProductDetailsUI,
        index: Int
    ) = withContext(dispatcher.default()) {
        val selectedColor = details.colors.getOrNull(index)
        val variantsForColor = details.variants.filter { it.colorValue() == selectedColor?.id }
        val variant = variantsForColor.firstOrNull { it.available } ?: variantsForColor.firstOrNull()
        details.copy(
            selectedColorUI = selectedColor,
            isSelectionSoldOut = details.variants.isSoldOut(colorId = selectedColor?.id),
            sizeSectionUI = details.variants.toSizeSectionUI(selectedColor),
            shareInfo = buildProductDetailsShareInfo(
                brand = details.brand,
                name = details.name,
                slug = details.slug,
                price = variant?.price?.amount?.amountFormatted.orEmpty()
            ),
            // Fall back to the existing gallery when the selected colour has no media,
            // mirroring galleryFor()'s "never blank" behaviour on initial load.
            gallery = variant?.media
                ?.takeIf { it.isNotEmpty() }
                ?.toGalleryUI()
                ?: details.gallery
        )
    }

    suspend fun setSelectedSize(
        details: ProductDetailsUI,
        sizeUI: SizeUI
    ) = withContext(dispatcher.default()) {
        details.copy(
            sizeSectionUI = when (details.sizeSectionUI) {
                is SizeSectionUI.SizeModalPicker -> details.sizeSectionUI.copy(
                    selectedSize = sizeUI
                )
                is SizeSectionUI.SizeSelector -> details.sizeSectionUI.copy(
                    selectedSize = sizeUI
                )
                else -> details.sizeSectionUI
            }
        )
    }

    suspend fun getSelectedVariantSku(
        details: ProductDetailsUI
    ): String? = withContext(dispatcher.default()) {
        val selectedColorId = details.selectedColorUI?.id ?: return@withContext null
        val selectedSizeId = (details.sizeSectionUI as? SizeSectionUI.SizeSelector)?.selectedSize?.id
            ?: (details.sizeSectionUI as? SizeSectionUI.SizeModalPicker)?.selectedSize?.id
            ?: return@withContext null

        val selectedVariant = details.variants.firstOrNull {
            it.colorValue() == selectedColorId && it.sizeValue() == selectedSizeId
        } ?: return@withContext null

        return@withContext selectedVariant.sku
    }

    private fun List<ColorUI>.findSelected(defaultVariant: Variant?): ColorUI? {
        val defaultColor = defaultVariant?.colorValue() ?: return firstOrNull()
        return firstOrNull { it.id == defaultColor } ?: firstOrNull()
    }

    private fun Product.resolveDefaultVariant(): Variant? =
        variants.firstOrNull { it.id == defaultVariantId }
            ?: variants.firstOrNull { it.available }
            ?: variants.firstOrNull()

    private fun Product.galleryFor(selectedColorId: String?, defaultVariant: Variant?): GalleryUI {
        val variantImages = variants.firstOrNull { it.colorValue() == selectedColorId }?.media
            ?: defaultVariant?.media
            ?: emptyList()
        val source = variantImages.ifEmpty { images }
        return source.toGalleryUI()
    }

    private fun Variant.colorValue(): String? = options
        .firstOrNull { it.name.equalsIgnoreCase(OPTION_NAME_COLOR) || it.name.equalsIgnoreCase(OPTION_NAME_COLOUR) }
        ?.value

    private fun Variant.sizeValue(): String? = options
        .firstOrNull { it.name.equalsIgnoreCase(OPTION_NAME_SIZE) }
        ?.value

    private fun List<Variant>.isSoldOut(colorId: String?) =
        filter { it.colorValue() == colorId }.isSoldOut()

    private fun List<Variant>.isSoldOut() = isNotEmpty() && all { !it.available }

    private fun Product.mapColors(): List<ColorUI> = buildList {
        variants.groupBy { it.colorValue() }
            .filterKeys { !it.isNullOrBlank() }
            .onEachIndexed { index, entry ->
                val colorName = entry.key ?: return@onEachIndexed
                val isEnabled = !entry.value.isSoldOut()
                val type = colorName.toSwatchType(isEnabled)
                add(
                    ColorUI(
                        id = colorName,
                        type = type,
                        index = index
                    )
                )
            }
    }

    private fun String.toSwatchType(isEnabled: Boolean): SwatchType {
        val hex = ColorNameToHex.lookup(this)
        return SwatchType.PlainColor(
            color = hex?.let { ComposeColor(it) } ?: Theme.color.black,
            isEnabled = isEnabled
        )
    }

    private fun buildShareText(
        brand: String,
        name: String,
        price: String,
        url: String
    ): StringResource = StringResource.fromId(
        id = R.string.product_details_share_text,
        args = listOf(brand, name, price, url)
    )

    private fun getSectionsList(environment: Environment): ImmutableList<ProductDetailsSectionItem> = persistentListOf(
        ProductDetailsSectionItem(
            title = StringResource.fromId(R.string.product_details_section_delivery_and_returns),
            url = "${environment.webUrl}/$DELIVERY_RETURNS_URL"
        ),
        ProductDetailsSectionItem(
            title = StringResource.fromId(R.string.product_details_section_payment_options),
            url = "${environment.webUrl}/$PAYMENT_OPTIONS_URL"
        )
    )

    private fun List<Media.Image>.toGalleryUI(): GalleryUI = GalleryUI(
        medias = map<Media.Image, MediaUI> { it.toImageUI() }.toImmutableList()
    )

    private fun Media.Image.toImageUI(): ImageUI = ImageUI(
        images = persistentListOf(
            ImageSizeUI.Custom(
                url = url
            )
        ),
        alt = alt
    )

    private fun List<Variant>.toSizeSectionUI(selectedColorUI: ColorUI?): SizeSectionUI {
        val selectedVariantsForColor = this.filter { it.colorValue() == selectedColorUI?.id }
        val productHasStock = this.any { it.available }
        val isSingleSize = this.none { it.sizeValue() != null }
        return when {
            !productHasStock -> SizeSectionUI.NoSize
            isSingleSize -> SizeSectionUI.SingleSize
            selectedVariantsForColor.size == 1 -> SizeSectionUI.SizeOnly(sizeUI = selectedVariantsForColor.first().toSizeUI())
            selectedVariantsForColor.size <= SIZE_SELECTOR_THRESHOLD -> SizeSectionUI.SizeSelector(
                sizes = selectedVariantsForColor.map { it.toSizeUI() }.toImmutableList()
            )
            else -> SizeSectionUI.SizeModalPicker(sizes = selectedVariantsForColor.map { it.toSizeUI() }.toImmutableList())
        }
    }

    private fun Variant.toSizeUI(): SizeUI {
        val sizeValue = sizeValue()
        return SizeUI(
            id = sizeValue.orEmpty(),
            properties = if (sizeValue == null) {
                SizingButtonProperties.EMPTY
            } else {
                SizingButtonProperties(
                    text = sizeValue,
                    state = if (available) SizingButtonState.Selectable else SizingButtonState.OutOfStock
                )
            }
        )
    }

    private suspend fun buildProductDetailsShareInfo(
        brand: String,
        name: String,
        slug: String,
        price: String
    ): ProductDetailsShareInfo {
        val environment = environmentManager.current()

        return ProductDetailsShareInfo(
            name = name,
            content = buildShareText(
                brand = brand,
                name = name,
                price = price,
                url = "${environment.webUrl}/$slug"
            )
        )
    }
}

private val MULTI_NEWLINE_REGEX = Regex("\n{3,}")

/**
 * Converts an HTML description to display plain text: strips tags and decodes HTML entities
 * (e.g. `&amp;` → `&`, `&pound;` → `£`) via [HtmlCompat], collapsing block elements to newlines.
 */
internal fun String?.stripHtml(): String {
    if (this.isNullOrBlank()) return ""
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
        .toString()
        .replace('\u00A0', ' ') // normalise non-breaking spaces (from &nbsp;) to regular spaces
        .let { MULTI_NEWLINE_REGEX.replace(it, "\n\n") }
        .trim()
}

private fun String.equalsIgnoreCase(other: String): Boolean = this.equals(other, ignoreCase = true)
