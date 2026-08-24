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
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonProperties
import com.mindera.alfie.designsystem.component.sizingbutton.SizingButtonState
import com.mindera.alfie.designsystem.component.swatch.SwatchType
import com.mindera.alfie.feature.pdp.model.ColorUI
import com.mindera.alfie.feature.pdp.model.ProductDetailsSectionItem
import com.mindera.alfie.feature.pdp.model.ProductDetailsShareInfo
import com.mindera.alfie.feature.pdp.model.ProductDetailsUI
import com.mindera.alfie.feature.pdp.model.SizeSectionUI
import com.mindera.alfie.feature.pdp.model.SizeUI
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
import com.mindera.alfie.repository.product.model.colorOptionValue
import com.mindera.alfie.repository.product.model.resolveDefaultVariant
import com.mindera.alfie.repository.product.model.sizeOptionValue
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
        internal const val DELIVERY_RETURNS_URL = "return-options"
        internal const val PAYMENT_OPTIONS_URL = "payment-options"

        val LOADING = ProductDetailsUI(
            id = "",
            brand = "",
            name = "",
            slug = "",
            description = "",
            price = PriceType.Default(""),
            colors = persistentListOf(
                ColorUI(
                    id = "",
                    type = SwatchType.PlainColor(ComposeColor.Transparent),
                    index = 0
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
        val base = ProductDetailsUI(
            id = product.id,
            brand = product.brandName.orEmpty(),
            name = product.name,
            slug = product.slug,
            description = product.descriptionHtml.stripHtml(),
            price = PriceType.Default(""),
            colors = colors.toImmutableList(),
            variants = product.variants.toImmutableList(),
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
        base.copy(price = base.displayVariant().toPriceType())
    }

    suspend fun setSelectedColour(
        details: ProductDetailsUI,
        index: Int
    ) = withContext(dispatcher.default()) {
        val selectedColor = details.colors.getOrNull(index)
        val variantsForColor = details.variants.filter { it.colorOptionValue == selectedColor?.id }
        val variant = variantsForColor.firstOrNull { it.available } ?: variantsForColor.firstOrNull()
        val updated = details.copy(
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
        updated.copy(price = updated.displayVariant().toPriceType())
    }

    suspend fun setSelectedSize(
        details: ProductDetailsUI,
        sizeUI: SizeUI
    ) = withContext(dispatcher.default()) {
        val updated = details.copy(
            sizeSectionUI = (details.sizeSectionUI as? SizeSectionUI.SizeSelector)
                ?.copy(selectedSize = sizeUI)
                ?: details.sizeSectionUI
        )
        updated.copy(price = updated.displayVariant().toPriceType())
    }

    suspend fun getSelectedVariantSku(
        details: ProductDetailsUI
    ): String? = withContext(dispatcher.default()) {
        val selectedSize = (details.sizeSectionUI as? SizeSectionUI.SizeSelector)?.selectedSize

        // A product with a size grid must have a size picked before anything can be added —
        // the disabled CTA enforces it, this keeps the factory honest about it too.
        if (details.sizeSectionUI is SizeSectionUI.SizeSelector && selectedSize == null) {
            return@withContext null
        }

        val selectedColorId = details.selectedColorUI?.id
        if (selectedColorId != null && selectedSize != null) {
            // Products can carry options beyond colour+size (e.g. "Sleeve length type"); prefer an
            // in-stock sibling so a mixed-availability size still adds a purchasable variant.
            val matches = details.variants.filter {
                it.colorOptionValue == selectedColorId && it.sizeOptionValue == selectedSize.id
            }
            (matches.firstOrNull { it.available } ?: matches.firstOrNull())?.sku
                ?.let { return@withContext it }
        }

        // No size choice applies (SingleSize / SizeOnly) or the product carries no colour
        // option — the display variant is the selection.
        details.displayVariant()?.sku
    }

    private fun List<ColorUI>.findSelected(defaultVariant: Variant?): ColorUI? {
        val defaultColor = defaultVariant?.colorOptionValue ?: return firstOrNull()
        return firstOrNull { it.id == defaultColor } ?: firstOrNull()
    }

    private fun Product.galleryFor(selectedColorId: String?, defaultVariant: Variant?): GalleryUI {
        val variantImages = variants.firstOrNull { it.colorOptionValue == selectedColorId }?.media
            ?: defaultVariant?.media
            ?: emptyList()
        val source = variantImages.ifEmpty { images }
        return source.toGalleryUI()
    }

    private fun List<Variant>.isSoldOut(colorId: String?) =
        filter { it.colorOptionValue == colorId }.isSoldOut()

    private fun List<Variant>.isSoldOut() = isNotEmpty() && all { !it.available }

    private fun Product.mapColors(): List<ColorUI> = buildList {
        variants.groupBy { it.colorOptionValue }
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
            color = hex?.let { ComposeColor(it) } ?: ComposeColor.Black,
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

    private fun Variant?.toPriceType(): PriceType {
        val variant = this ?: return PriceType.Default("")
        val salePrice = variant.price.amount
        val fullPrice = variant.price.was
        return if (fullPrice != null && fullPrice.amount > salePrice.amount) {
            PriceType.Sale(
                fullPrice = fullPrice.amountFormatted,
                salePrice = salePrice.amountFormatted
            )
        } else {
            PriceType.Default(salePrice.amountFormatted)
        }
    }

    private fun List<Variant>.toSizeSectionUI(selectedColorUI: ColorUI?): SizeSectionUI {
        // Products can carry options beyond colour+size (e.g. "Sleeve length type"); collapse
        // those sibling variants into one chip per size, preferring an in-stock representative.
        val uniqueSizes = filter { it.colorOptionValue == selectedColorUI?.id }
            .sortedByDescending { it.available }
            .distinctBy { it.sizeOptionValue }
        val productHasStock = any { it.available }
        val isSingleSize = none { it.sizeOptionValue != null }
        return when {
            !productHasStock -> SizeSectionUI.NoSize
            isSingleSize -> SizeSectionUI.SingleSize
            uniqueSizes.size == 1 -> SizeSectionUI.SizeOnly(sizeUI = uniqueSizes.first().toSizeUI())
            else -> SizeSectionUI.SizeSelector(
                sizes = uniqueSizes.map { it.toSizeUI() }.toImmutableList()
            )
        }
    }

    private fun Variant.toSizeUI(): SizeUI {
        val sizeValue = sizeOptionValue
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
