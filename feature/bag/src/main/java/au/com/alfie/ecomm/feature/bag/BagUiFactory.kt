package au.com.alfie.ecomm.feature.bag

import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.bag.models.BagProductUi
import au.com.alfie.ecomm.feature.mappers.mapImage
import au.com.alfie.ecomm.feature.mappers.mapPrice
import au.com.alfie.ecomm.repository.bag.BagProduct
import au.com.alfie.ecomm.repository.product.model.Product
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

internal class BagUiFactory @Inject constructor() {

    operator fun invoke(
        bagProducts: List<BagProduct>,
        products: List<Product>,
        onRemoveClick: ClickEventOneArg<BagProduct>
    ): ImmutableList<BagProductUi> = bagProducts.mapNotNull { bagProduct ->
        val product = products.find { it.id == bagProduct.productId } ?: return@mapNotNull null
        val selectedVariant = product.variants.find { it.sku == bagProduct.variantSku } ?: return@mapNotNull null
        BagProductUi(
            productCardData = product.copy(defaultVariant = selectedVariant).mapProductCardData(onRemoveClick)
        )
    }.toImmutableList()

    private fun Product.mapProductCardData(onRemoveClick: (BagProduct) -> Unit) = ProductCardType.XSmall(
        brand = brand.name,
        name = name,
        price = mapPrice(
            priceRange = priceRange,
            price = defaultVariant.price
        ),
        image = defaultVariant.media.mapImage(),
        color = defaultVariant.color?.name.orEmpty(),
        size = defaultVariant.size?.value.orEmpty(),
        onRemoveClick = { onRemoveClick(BagProduct(productId = id, variantSku = defaultVariant.sku)) }
    )
}