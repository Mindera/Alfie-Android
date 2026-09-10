package com.mindera.alfie.feature.bag

import com.mindera.alfie.core.commons.string.formatMoney
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.bag.models.BagContentUi
import com.mindera.alfie.feature.bag.models.BagItemNotice
import com.mindera.alfie.feature.bag.models.BagProductUi
import com.mindera.alfie.feature.bag.models.BagSummaryUi
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.feature.mappers.toPriceType
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
import com.mindera.alfie.repository.product.model.colorOptionValue
import com.mindera.alfie.repository.product.model.resolveDefaultVariant
import com.mindera.alfie.repository.product.model.sizeOptionValue
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

/**
 * At or below this many units left, the line surfaces the low-stock notice. Figma shows the message
 * reading "Only 2 items left!" but does not state the threshold, so this is a product assumption.
 */
internal const val LOW_STOCK_THRESHOLD = 5

internal class BagUiFactory @Inject constructor() {

    operator fun invoke(
        bagProducts: List<BagProduct>,
        products: List<Product>,
        onProductClick: ClickEventOneArg<String>
    ): BagContentUi {
        // The bag stores one entry per unit, so entries for the same variant collapse into a single
        // line whose quantity is the number of entries. groupBy keeps first-added order.
        val lines = bagProducts
            .groupBy { it }
            .mapNotNull { (bagProduct, entries) ->
                // A product whose fetch failed is simply skipped: the previous `first { }` threw and
                // took the whole screen down with it.
                val product = products.firstOrNull { it.slug == bagProduct.productId }
                    ?: return@mapNotNull null
                BagLine(
                    bagProduct = bagProduct,
                    product = product,
                    variant = product.variantFor(bagProduct.variantSku),
                    quantity = entries.size
                )
            }

        return BagContentUi(
            items = lines.map { line -> line.toBagProductUi(onProductClick) }.toImmutableList(),
            summary = lines.toSummary()
        )
    }

    private fun Product.variantFor(variantSku: String): Variant? =
        variants.firstOrNull { it.sku == variantSku } ?: resolveDefaultVariant()

    private fun BagLine.toBagProductUi(
        onProductClick: ClickEventOneArg<String>
    ) = BagProductUi(
        // Quantity is grouped per variant, so the SKU has to be part of the list key.
        id = "${bagProduct.productId}-${bagProduct.variantSku}",
        bagProduct = bagProduct,
        notice = variant.toNotice(),
        productCardData = ProductCardType.Horizontal(
            image = variant?.media?.firstOrNull().toImageUI(),
            brand = product.brandName.orEmpty(),
            name = product.name,
            // The variant's own price, not the product's range: a bag line is one specific variant,
            // and the summary total has to agree with what each line shows.
            price = variant?.price?.toPriceType() ?: PriceType.Default(price = ""),
            color = variant?.colorOptionValue.orEmpty(),
            size = variant?.sizeOptionValue.orEmpty(),
            reference = variant?.sku.orEmpty(),
            quantity = quantity,
            isAvailable = variant?.available == true,
            onClick = { onProductClick(product.slug) }
        )
    )

    private fun Variant?.toNotice(): BagItemNotice? = when {
        this == null -> null
        availableQuantity <= 0 -> BagItemNotice.Unavailable
        availableQuantity <= LOW_STOCK_THRESHOLD -> BagItemNotice.LowStock(remaining = availableQuantity)
        else -> null
    }

    private fun List<BagLine>.toSummary(): BagSummaryUi {
        val total = sumOf { line -> (line.variant?.price?.amount?.amount ?: .0) * line.quantity }
        val currencyCode = firstNotNullOfOrNull { line -> line.variant?.price?.amount?.currencyCode }
        return BagSummaryUi(
            totalFormatted = currencyCode
                ?.let { code -> formatMoney(amount = total, currencyCode = code) }
                .orEmpty()
        )
    }

    private data class BagLine(
        val bagProduct: BagProduct,
        val product: Product,
        val variant: Variant?,
        val quantity: Int
    )
}
