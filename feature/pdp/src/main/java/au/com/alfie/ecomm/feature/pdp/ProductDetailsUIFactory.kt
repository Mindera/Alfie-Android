package au.com.alfie.ecomm.feature.pdp

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.core.commons.extension.isNotNullOrBlank
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.core.environment.model.Environment
import au.com.alfie.ecomm.core.ui.media.GalleryUI
import au.com.alfie.ecomm.core.ui.media.MediaUI
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.core.ui.media.video.VideoPreviewImageUI
import au.com.alfie.ecomm.core.ui.media.video.VideoSourceUI
import au.com.alfie.ecomm.core.ui.media.video.VideoUI
import au.com.alfie.ecomm.designsystem.component.sizingbutton.SizingButtonProperties
import au.com.alfie.ecomm.designsystem.component.sizingbutton.SizingButtonState
import au.com.alfie.ecomm.designsystem.component.swatch.SwatchType
import au.com.alfie.ecomm.designsystem.component.tab.TabItem
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.pdp.model.ColorUI
import au.com.alfie.ecomm.feature.pdp.model.InformationUI
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsSectionItem
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsShareInfo
import au.com.alfie.ecomm.feature.pdp.model.ProductDetailsUI
import au.com.alfie.ecomm.feature.pdp.model.SizeSectionUI
import au.com.alfie.ecomm.feature.pdp.model.SizeUI
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.product.model.Variant
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.Size
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

        val LOADING = ProductDetailsUI(
            id = "",
            brand = "",
            name = "",
            slug = "",
            shortDescription = "",
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
        val colors = product.mapColors()
        val selectedColor = colors.find { it.isSelected(product.defaultVariant) }
        val description = product.longDescription.orEmpty()
        val descriptionItem = InformationUI.Description(
            tabItem = TabItem(StringResource.fromId(R.string.product_details_information_description)),
            content = description
        )
        ProductDetailsUI(
            id = product.id,
            brand = product.brand.name,
            name = product.name,
            slug = product.slug,
            shortDescription = product.shortDescription,
            information = persistentListOf(descriptionItem),
            variants = product.variants.toImmutableList(),
            colors = colors.toImmutableList(),
            selectedColorUI = selectedColor,
            isSelectionSoldOut = product.variants.isSoldOut(selectedColor?.id),
            sections = getSectionsList(environment = environment),
            shareInfo = product.buildProductDetailsShareInfo(price = product.defaultVariant.price.amount.amountFormatted),
            gallery = product.defaultVariant.color?.media?.toGalleryUI() ?: GalleryUI(emptyList<MediaUI>().toImmutableList()),
            sizeSectionUI = product.variants.toSizeSectionUI(selectedColor)
        )
    }

    suspend fun setSelectedColour(
        details: ProductDetailsUI,
        index: Int
    ) = withContext(dispatcher.default()) {
        val selectedColor = details.colors.getOrNull(index)
        val variant = details.variants.first { it.color?.id == selectedColor?.id }
        details.copy(
            selectedColorUI = selectedColor,
            isSelectionSoldOut = details.variants.isSoldOut(colorId = selectedColor?.id),
            sizeSectionUI = details.variants.toSizeSectionUI(selectedColor),
            shareInfo = details.buildProductDetailsShareInfo(variant.price.amount.amountFormatted),
            gallery = variant.color?.media.orEmpty().toGalleryUI()
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

    private fun ColorUI.isSelected(defaultVariant: Variant): Boolean = defaultVariant.color?.id == this.id

    private fun List<Variant>.isSoldOut(colorId: String?) = filter { it.color?.id == colorId }.isSoldOut()

    private fun List<Variant>.isSoldOut() = all { it.stock < 1 }

    private fun Product.mapColors(): List<ColorUI> = buildList {
        variants.groupBy { it.color?.id }
            .onEachIndexed { index, entry ->
                val isEnabled = entry.value.isSoldOut().not()
                val color = entry.value.firstOrNull()?.color ?: return@onEachIndexed

                val type = if (color.swatch?.url.isNotNullOrBlank()) {
                    SwatchType.Image(
                        url = color.swatch?.url.orEmpty(),
                        isEnabled = isEnabled
                    )
                } else {
                    SwatchType.PlainColor(
                        color = Theme.color.black,
                        isEnabled = isEnabled
                    )
                }
                add(
                    ColorUI(
                        id = color.id,
                        type = type,
                        index = index
                    )
                )
            }
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

    private fun List<Media?>.toGalleryUI(): GalleryUI = GalleryUI(
        medias = map {
            if (it is Media.Image) {
                it.toImageUI()
            } else {
                (it as Media.Video).toVideoUI()
            }
        }.toImmutableList()
    )

    private fun Media.Image.toImageUI(): ImageUI = ImageUI(
        images = persistentListOf(
            ImageSizeUI.Custom(
                url = url,
            )
        ),
        alt = alt
    )

    private fun Media.Video.toVideoUI(): VideoUI = VideoUI(
        previewImage = VideoPreviewImageUI(
            url = previewImage?.url.orEmpty(),
            alt = previewImage?.alt.orEmpty()
        ),
        source = VideoSourceUI(url = this.sources.firstOrNull()?.url.orEmpty())
    )

    private fun List<Variant>.toSizeSectionUI(selectedColorUI: ColorUI?): SizeSectionUI {
        val selectedVariantsForColor = this.filter { it.color?.id == selectedColorUI?.id }
        val productStockCount = this.fold(0) { acc: Int, variant: Variant -> acc + variant.stock }
        val isSingleSize = this.none { it.size != null }
        return when {
            productStockCount == 0 -> SizeSectionUI.NoSize
            isSingleSize -> SizeSectionUI.SingleSize
            selectedVariantsForColor.size == 1 -> SizeSectionUI.SizeOnly(sizeUI = selectedVariantsForColor.first().toSizeUI())
            selectedVariantsForColor.size <= SIZE_SELECTOR_THRESHOLD -> SizeSectionUI.SizeSelector(
                sizes = selectedVariantsForColor.map { it.toSizeUI() }.toImmutableList()
            )
            else -> SizeSectionUI.SizeModalPicker(sizes = selectedVariantsForColor.map { it.toSizeUI() }.toImmutableList())
        }
    }

    private fun Variant.toSizeUI(): SizeUI {
        val hasStock = stock != 0
        return SizeUI(
            id = size?.id.orEmpty(),
            properties = size?.toSizingButtonProperties(hasStock) ?: SizingButtonProperties.EMPTY
        )
    }

    private fun Size.toSizingButtonProperties(hasStock: Boolean): SizingButtonProperties = SizingButtonProperties(
        text = value,
        state = if (hasStock) SizingButtonState.Selectable else SizingButtonState.OutOfStock
    )

    private suspend fun Product.buildProductDetailsShareInfo(price: String): ProductDetailsShareInfo {
        val environment = environmentManager.current()

        return ProductDetailsShareInfo(
            name = name,
            content = buildShareText(
                brand = brand.name,
                name = name,
                price = price,
                url = "${environment.webUrl}/$slug"
            )
        )
    }

    private suspend fun ProductDetailsUI.buildProductDetailsShareInfo(price: String): ProductDetailsShareInfo {
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
