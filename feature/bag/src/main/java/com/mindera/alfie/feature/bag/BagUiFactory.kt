package com.mindera.alfie.feature.bag

import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.bag.models.BagProductUi
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.feature.mappers.toPriceType
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
import com.mindera.alfie.repository.product.model.resolveDefaultVariant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

internal class BagUiFactory @Inject constructor() {

    operator fun invoke(
        bagProducts: List<BagProduct>,
        products: List<Product>,
        onRemoveClick: ClickEventOneArg<BagProduct>,
        onProductClick: ClickEventOneArg<String>
    ): ImmutableList<BagProductUi> = bagProducts.map { item ->
        val product = products.first { it.slug == item.productId }
        val selectedVariant = product.variants.find { it.sku == item.variantSku } ?: product.resolveDefaultVariant()
        val productCard = product.toProductCard(
            bagProduct = item,
            variant = selectedVariant,
            onRemoveClick = onRemoveClick,
            onProductClick = onProductClick
        )

        BagProductUi(
            id = item.productId,
            productCardData = productCard
        )
    }.toImmutableList()

    private fun Product.toProductCard(
        bagProduct: BagProduct,
        variant: Variant?,
        onRemoveClick: ClickEventOneArg<BagProduct>,
        onProductClick: ClickEventOneArg<String>
    ) = ProductCardType.Horizontal(
        brand = brandName.orEmpty(),
        name = name,
        price = priceRange.toPriceType(variant?.price),
        image = variant?.media?.firstOrNull().toImageUI(),
        color = variant?.colorOption().orEmpty(),
        size = variant?.sizeOption().orEmpty(),
        onClick = { onProductClick(slug) },
        onRemoveClick = { onRemoveClick(bagProduct) }
    )

    private fun Variant.colorOption(): String? =
        options.firstOrNull { it.name.equals("color", ignoreCase = true) || it.name.equals("colour", ignoreCase = true) }?.value

    private fun Variant.sizeOption(): String? =
        options.firstOrNull { it.name.equals("size", ignoreCase = true) }?.value
}