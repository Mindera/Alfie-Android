package com.mindera.alfie.feature.bag

import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.bag.models.BagProductUi
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.feature.mappers.toPriceType
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.product.model.Product
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

    private fun Product.toProductCard(onRemoveClick: ClickEventOneArg<BagProduct>) = ProductCardType.Horizontal(
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