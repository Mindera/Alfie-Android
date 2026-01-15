package au.com.alfie.ecomm.feature.bag

import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.bag.models.BagProductUi
import au.com.alfie.ecomm.feature.mappers.toImageUI
import au.com.alfie.ecomm.feature.mappers.toPriceType
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
    ): ImmutableList<BagProductUi> = bagProducts.map { item ->
        val product = products.first { it.id == item.productId }
        val selectedVariant = product.variants.find { it.sku == item.variantSku } ?: product.defaultVariant
        val productCard = product.copy(defaultVariant = selectedVariant).toProductCard(onRemoveClick)

        BagProductUi(
            id = item.productId,
            productCardData = productCard
        )
    }.toImmutableList()

    private fun Product.toProductCard(onRemoveClick: ClickEventOneArg<BagProduct>) = ProductCardType.XSmall(
        brand = brand.name,
        name = name,
        price = priceRange.toPriceType(defaultVariant.price),
        image = defaultVariant.media.toImageUI(),
        color = defaultVariant.color?.name.orEmpty(),
        size = defaultVariant.size?.value.orEmpty(),
        onRemoveClick = {
            onRemoveClick(
                BagProduct(
                    productId = id,
                    variantSku = defaultVariant.sku
                )
            )
        }
    )
}