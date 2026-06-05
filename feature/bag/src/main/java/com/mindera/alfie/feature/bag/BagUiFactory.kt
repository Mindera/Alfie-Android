package com.mindera.alfie.feature.bag

import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.bag.models.BagProductUi
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.feature.mappers.toPriceType
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
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
        val product = products.first { it.id == item.productId }
        val selectedVariant = product.variants.find { it.sku == item.variantSku } ?: product.resolveDefaultVariant()
        val productCard = product.toProductCard(
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
        variant: Variant,
        onRemoveClick: ClickEventOneArg<BagProduct>,
        onProductClick: ClickEventOneArg<String>
    ) = ProductCardType.Horizontal(
        brand = brandName.orEmpty(),
        name = name,
        price = priceRange.toPriceType(variant.price),
        image = variant.media.firstOrNull().toImageUI(),
        // TODO(ALFMOB-388): bag stores productId, not handle/slug. PDP requires handle now;
        // bag domain migration is out of scope for ALFMOB-338.
        color = variant.colorOption().orEmpty(),
        size = variant.sizeOption().orEmpty(),
        onClick = { onProductClick(id) },
        onRemoveClick = {
            onRemoveClick(
                BagProduct(
                    productId = id,
                    variantSku = variant.sku
                )
            )
        }
    )

    private fun Product.resolveDefaultVariant(): Variant =
        variants.firstOrNull { it.id == defaultVariantId }
            ?: variants.firstOrNull { it.available }
            ?: variants.first()

    private fun Variant.colorOption(): String? =
        options.firstOrNull { it.name.equals("color", ignoreCase = true) || it.name.equals("colour", ignoreCase = true) }?.value

    private fun Variant.sizeOption(): String? =
        options.firstOrNull { it.name.equals("size", ignoreCase = true) }?.value
}