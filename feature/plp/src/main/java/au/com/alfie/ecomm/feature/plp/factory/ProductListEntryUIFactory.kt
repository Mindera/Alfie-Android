package au.com.alfie.ecomm.feature.plp.factory

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.plp.model.ProductListEntryUI
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntry
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import kotlinx.coroutines.withContext
import au.com.alfie.ecomm.feature.mappers.mapPrice
import au.com.alfie.ecomm.feature.mappers.mapImage
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
        image = mapImage(entry.defaultVariant.defaultMedia),
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
        image = mapImage(entry.defaultVariant.defaultMedia),
        onFavoriteClick = onFavoriteClick
    )
}
