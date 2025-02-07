package au.com.alfie.ecomm.feature.wishlist

import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import javax.inject.Inject
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.feature.mappers.mapPrice
import au.com.alfie.ecomm.feature.mappers.mapImage
import au.com.alfie.ecomm.feature.wishlist.models.WishlistProductUi

class WishlistUiFactory @Inject constructor() {

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


    private fun mapProductCardData(
        product: Product,
        onRemoveClick: ClickEvent
    ) = ProductCardType.WishList(
        brand = product.brand.name,
        name = product.name,
        price = mapPrice(
            priceRange = product.priceRange,
            price = product.defaultVariant.price
        ),
        image = product.defaultVariant.media.mapImage(),
        color = product.defaultVariant.color?.name ?: "",
        size = product.defaultVariant.size?.value ?: "",
        onRemoveClick = onRemoveClick
    )
}