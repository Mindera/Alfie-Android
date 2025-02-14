package au.com.alfie.ecomm.feature.bag

import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.bag.models.BagProductUi
import au.com.alfie.ecomm.feature.mappers.mapImage
import au.com.alfie.ecomm.feature.mappers.mapPrice
import au.com.alfie.ecomm.repository.bag.BagProduct
import au.com.alfie.ecomm.repository.product.model.Product
import javax.inject.Inject

class BagUiFactory @Inject constructor() {

    operator fun invoke(
        bagProducts: List<BagProduct>,
        products: List<Product>
    ): List<BagProductUi> = bagProducts.mapNotNull { bagProduct ->
        val product = products.find { it.id == bagProduct.productId } ?: return@mapNotNull null
        val selectedVariant = product.variants.find { it.sku == bagProduct.variantSku } ?: return@mapNotNull null

        BagProductUi(
            productCardData = product.copy(defaultVariant = selectedVariant).mapProductCardData()
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