package au.com.alfie.ecomm.feature.plp.factory

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.mappers.toImageUI
import au.com.alfie.ecomm.feature.mappers.toPriceType
import au.com.alfie.ecomm.feature.plp.model.ProductListEntryUI
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntry
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
