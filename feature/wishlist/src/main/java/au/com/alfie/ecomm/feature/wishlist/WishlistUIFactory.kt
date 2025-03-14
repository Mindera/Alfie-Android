package au.com.alfie.ecomm.feature.wishlist

import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.mappers.toImageUI
import au.com.alfie.ecomm.feature.mappers.toPriceType
import au.com.alfie.ecomm.feature.wishlist.models.WishlistProductUi
import au.com.alfie.ecomm.repository.product.model.Product
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class WishlistUIFactory @Inject constructor() {

    operator fun invoke(
        products: List<Product>,
        onRemoveClick: ClickEventOneArg<Product>
    ): ImmutableList<WishlistProductUi> =
        products.map {
            WishlistProductUi(
                productCardData = mapProductCardData(
                    product = it,
                    onRemoveClick = { onRemoveClick(it) }
                )
            )
        }.toImmutableList()

    private fun mapProductCardData(
        product: Product,
        onRemoveClick: ClickEvent
    ) = ProductCardType.Medium(
        brand = product.brand.name,
        name = product.name,
        price = product.priceRange.toPriceType(product.defaultVariant.price),
        image = product.defaultVariant.media.toImageUI(),
        onRemoveClick = onRemoveClick,
        color = product.defaultVariant.color?.name.orEmpty(),
        size = product.defaultVariant.size?.value.orEmpty()
    )
}