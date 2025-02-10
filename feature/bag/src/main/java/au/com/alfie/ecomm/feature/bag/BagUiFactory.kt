package au.com.alfie.ecomm.feature.bag

import androidx.compose.runtime.Immutable
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.bag.models.BagProductUi
import javax.inject.Inject
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.feature.mappers.mapPrice
import au.com.alfie.ecomm.feature.mappers.mapImage

class BagUiFactory @Inject constructor() {

    operator fun invoke(
        products: List<Product>,
    ): List<BagProductUi> = products.map {
        BagProductUi(
            productCardData = it.mapProductCardData()
        )
    }

    private fun Product.mapProductCardData() = ProductCardType.XSmall(
        brand = brand.name,
        name = name,
        price = mapPrice(
            priceRange = priceRange,
            price = defaultVariant.price
        ),
        image = defaultVariant.media.mapImage(),
        color = defaultVariant.color?.name.orEmpty(),
        size = defaultVariant.size?.value.orEmpty()
    )
}