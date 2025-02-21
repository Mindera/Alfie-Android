package au.com.alfie.ecomm.feature.wishlist

import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.mappers.mapImage
import au.com.alfie.ecomm.feature.mappers.mapPrice
import au.com.alfie.ecomm.feature.wishlist.models.WishlistProductUi
import au.com.alfie.ecomm.repository.product.model.Product
import javax.inject.Inject

class WishlistUIFactory @Inject constructor() {

    operator fun invoke(
        product: Product,
        onRemoveClick: ClickEvent
    ): WishlistProductUi =
        WishlistProductUi(
            productCardData = mapProductCardData(
                product = product,
                onRemoveClick = onRemoveClick
            )
        )

    fun mapProductCardData(
        product: Product,
        onRemoveClick: ClickEvent
    ) = ProductCardType.Medium(
        brand = product.brand.name,
        name = product.name,
        price = mapPrice(
            priceRange = product.priceRange,
            price = product.defaultVariant.price
        ),
        image = product.defaultVariant.media.mapImage(),
        onRemoveClick = onRemoveClick,
        color = product.defaultVariant.color?.name.orEmpty(),
        size = product.defaultVariant.size?.value.orEmpty()
    )
}