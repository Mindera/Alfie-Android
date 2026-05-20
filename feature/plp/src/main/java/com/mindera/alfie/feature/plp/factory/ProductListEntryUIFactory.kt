package com.mindera.alfie.feature.plp.factory

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.feature.mappers.toPriceType
import com.mindera.alfie.feature.plp.model.ProductListEntryUI
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ProductListEntryUIFactory @Inject constructor(
    private val dispatcher: DispatcherProvider
) {

    suspend operator fun invoke(
        entry: ProductListEntry,
        onFavoriteClick: ClickEvent,
        onProductClick: ClickEvent
    ): ProductListEntryUI = withContext(dispatcher.default()) {
        ProductListEntryUI(
            id = entry.id,
            productCardData = ProductCardType.Vertical(
                brand = entry.brand.name,
                name = entry.name,
                price = entry.priceRange.toPriceType(default = entry.defaultVariant.price),
                image = entry.defaultVariant.defaultMedia.toImageUI(),
                onClick = onProductClick,
                onFavoriteClick = onFavoriteClick
            )
        )
    }
}
