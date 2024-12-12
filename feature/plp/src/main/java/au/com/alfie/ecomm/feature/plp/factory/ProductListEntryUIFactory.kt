package au.com.alfie.ecomm.feature.plp.factory

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.core.commons.extension.orZero
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.plp.model.ProductListEntryUI
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.product.model.PriceRange
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntry
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import au.com.alfie.ecomm.repository.shared.model.Media
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ProductListEntryUIFactory @Inject constructor(
    private val dispatcher: DispatcherProvider
) {

    suspend operator fun invoke(
        entry: ProductListEntry,
        layoutMode: ProductListLayoutMode,
        onFavoriteClick: ClickEvent
    ): ProductListEntryUI = withContext(dispatcher.default()) {
        ProductListEntryUI(
            id = entry.id,
            productCardData = layoutMode.mapProductCardData(
                entry = entry,
                onFavoriteClick = onFavoriteClick
            )
        )
    }

    private fun ProductListLayoutMode.mapProductCardData(
        entry: ProductListEntry,
        onFavoriteClick: ClickEvent
    ) = when (this) {
        ProductListLayoutMode.GRID -> mapMediumProductCard(
            entry = entry,
            onFavoriteClick = onFavoriteClick
        )
        ProductListLayoutMode.COLUMN -> mapLargeProductCard(
            entry = entry,
            onFavoriteClick = onFavoriteClick
        )
    }

    private fun mapMediumProductCard(
        entry: ProductListEntry,
        onFavoriteClick: ClickEvent
    ) = ProductCardType.Medium(
        brand = entry.brand.name,
        name = entry.name,
        price = mapPrice(
            priceRange = entry.priceRange,
            price = entry.defaultVariant.price
        ),
        image = mapImage(entry.defaultVariant.media),
        onFavoriteClick = onFavoriteClick
    )

    private fun mapLargeProductCard(
        entry: ProductListEntry,
        onFavoriteClick: ClickEvent
    ) = ProductCardType.Large(
        brand = entry.brand.name,
        name = entry.name,
        price = mapPrice(
            priceRange = entry.priceRange,
            price = entry.defaultVariant.price
        ),
        image = mapImage(entry.defaultVariant.media),
        onFavoriteClick = onFavoriteClick
    )

    private fun mapPrice(
        priceRange: PriceRange?,
        price: Price
    ): PriceType {
        val rangeHigh = priceRange?.high
        val salePrice = price.was
        return when {
            rangeHigh != null -> PriceType.Range(
                startPrice = priceRange.low.amountFormatted,
                endPrice = rangeHigh.amountFormatted
            )
            salePrice != null -> PriceType.Sale(
                fullPrice = salePrice.amountFormatted,
                salePrice = price.amount.amountFormatted
            )
            else -> PriceType.Default(price = price.amount.amountFormatted)
        }
    }

    private fun mapImage(media: List<Media.Image>): ImageUI {
        val image = media.firstOrNull()
        val imageSizeUI = ImageSizeUI.Custom(
            url = image?.url.orEmpty(),
            width = image?.width.orZero()
        )
        return ImageUI(
            images = persistentListOf(imageSizeUI),
            alt = image?.alt
        )
    }
}
