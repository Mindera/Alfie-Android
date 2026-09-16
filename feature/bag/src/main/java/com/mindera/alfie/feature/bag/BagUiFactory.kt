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
import com.mindera.alfie.repository.product.model.sizeOptionValue
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

/**
 * At or below this many units left, the line surfaces the low-stock notice. The threshold is an
 * assumption — the design shows the message but does not say when it appears.
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

    // No fallback to the default variant: the bag line is identified by (productId, variantSku), so
    // substituting another variant would show its image, options and price under the saved line's
    // SKU — and feed the wrong number into the total. An unknown SKU resolves to null, which renders
    // the line as unavailable.
    private fun Product.variantFor(variantSku: String): Variant? =
        variants.firstOrNull { it.sku == variantSku }

    private fun BagLine.toBagProductUi(
        onProductClick: ClickEventOneArg<String>
    ) = BagProductUi(
        // Quantity is grouped per variant, so the SKU has to be part of the list key. Length-prefixed
        // because both halves may contain the separator: joining them plainly lets ("a-b", "c") and
        // ("a", "b-c") collapse to the same key, which the list rejects as a duplicate.
        id = "${bagProduct.productId.length}:${bagProduct.productId}:${bagProduct.variantSku}",
        bagProduct = bagProduct,
        notice = toNotice(),
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

    // Stock is read against the quantity the bag actually holds, not against the threshold alone:
    // three units of a variant with two left is a different problem from a shelf running low, and
    // saying "Only 2 items left!" under "Quantity: 3" reads as if nothing is wrong.
    private fun BagLine.toNotice(): BagItemNotice? = when {
        // A line with no resolvable variant is rendered unavailable by `isAvailable`, so it needs
        // the notice too — otherwise the row is dimmed with nothing explaining why.
        variant == null || variant.availableQuantity <= 0 -> BagItemNotice.Unavailable
        quantity > variant.availableQuantity -> BagItemNotice.ExceedsStock(remaining = variant.availableQuantity)
        variant.availableQuantity <= LOW_STOCK_THRESHOLD -> BagItemNotice.LowStock(remaining = variant.availableQuantity)
        else -> null
    }

    // Assumes one currency across the bag: the amounts are summed raw and formatted with the first
    // code the bag can offer. The catalogue is single-currency today; revisit if that ever stops
    // being true.
    //
    // An unresolvable line contributes nothing, because there is no price to contribute — it is
    // rendered unavailable and cannot be bought. An over-subscribed line is still charged in full:
    // the total has to agree with the arithmetic each row shows, and its notice carries the stock
    // problem.
    private fun List<BagLine>.toSummary(): BagSummaryUi {
        val total = sumOf { line -> (line.variant?.price?.amount?.amount ?: .0) * line.quantity }
        // Currency comes from the product's range when no variant resolved, so a bag of gone SKUs
        // still formats a zero total rather than drawing the "Total" label against an empty string.
        val currencyCode = firstNotNullOfOrNull { line -> line.variant?.price?.amount?.currencyCode }
            ?: firstNotNullOfOrNull { line -> line.product.priceRange?.low?.currencyCode }
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
