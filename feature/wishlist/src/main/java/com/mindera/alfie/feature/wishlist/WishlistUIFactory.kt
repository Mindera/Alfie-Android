package com.mindera.alfie.feature.wishlist

import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.feature.mappers.toPriceType
import com.mindera.alfie.feature.wishlist.models.WishlistProductUi
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class WishlistUIFactory @Inject constructor() {

    operator fun invoke(
        products: List<Product>,
        onRemoveClick: ClickEventOneArg<Product>,
        onAddToBagClick: ClickEventOneArg<Product>,
        onProductClick: ClickEventOneArg<Product>
    ): ImmutableList<WishlistProductUi> =
        products.map {
            WishlistProductUi(
                productCardData = mapProductCardData(
                    product = it,
                    onRemoveClick = { onRemoveClick(it) },
                    onAddToBagClick = { onAddToBagClick(it) },
                    onClick = { onProductClick(it) }
                )
            )
        }.toImmutableList()

    private fun mapProductCardData(
        product: Product,
        onRemoveClick: ClickEvent,
        onAddToBagClick: ClickEvent,
        onClick: ClickEvent
    ): ProductCardType.Vertical {
        val defaultVariant = product.resolveDefaultVariant()
        return ProductCardType.Vertical(
            brand = product.brandName.orEmpty(),
            name = product.name,
            price = product.priceRange.toPriceType(defaultVariant?.price),
            image = defaultVariant?.media?.firstOrNull().toImageUI(),
            onClick = onClick,
            onRemoveClick = onRemoveClick,
            addToBagClick = onAddToBagClick
        )
    }

    private fun Product.resolveDefaultVariant(): Variant? =
        variants.firstOrNull { it.id == defaultVariantId }
            ?: variants.firstOrNull { it.available }
            ?: variants.firstOrNull()
}