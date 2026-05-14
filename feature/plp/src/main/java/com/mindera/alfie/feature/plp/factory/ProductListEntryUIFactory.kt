package com.mindera.alfie.feature.plp.factory

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.feature.mappers.toPriceType
import com.mindera.alfie.feature.plp.model.ProductListEntryUI
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
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
        price = entry.priceRange.toPriceType(default = entry.defaultVariant.price),
        image = entry.defaultVariant.defaultMedia.toImageUI(),
        onFavoriteClick = onFavoriteClick
    )

    private fun mapLargeProductCard(
        entry: ProductListEntry,
        onFavoriteClick: ClickEvent
    ) = ProductCardType.Large(
        brand = entry.brand.name,
        name = entry.name,
        price = entry.priceRange.toPriceType(default = entry.defaultVariant.price),
        image = entry.defaultVariant.defaultMedia.toImageUI(),
        onFavoriteClick = onFavoriteClick
    )
}
