package com.mindera.alfie.feature.bag

import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.feature.bag.models.BagContentUi
import com.mindera.alfie.feature.bag.models.BagItemNotice
import com.mindera.alfie.feature.bag.models.BagLineUi
import com.mindera.alfie.feature.bag.models.BagSummaryUi
import com.mindera.alfie.feature.mappers.cardImageFor
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.feature.mappers.toPriceType
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.bag.BagLine
import com.mindera.alfie.repository.product.model.Price
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

/**
 * Builds the Bag screen from the Cart and the products behind its lines.
 *
 * The Cart owns quantity, prices and the total. It has no brand, colour, size, was-price or stock,
 * so those come from the product fetched by the line's slug (ADR 0001). A line whose product
 * didn't load still shows, with the Cart's own name, image and price.
 */
internal class BagUiFactory @Inject constructor() {

    operator fun invoke(
        bag: Bag,
        productsBySlug: Map<String, Product>,
        onProductClick: ClickEventOneArg<String>
    ): BagContentUi = BagContentUi(
        items = bag.lines.map { line ->
            val product = line.slug?.let(productsBySlug::get)
            line.toBagLineUi(
                product = product,
                variant = product?.variantFor(line),
                onProductClick = onProductClick
            )
        }.toImmutableList(),
        summary = BagSummaryUi(totalFormatted = bag.subtotal?.amountFormatted)
    )

    // Matched by variant id, which is what the Cart line carries; SKU is the fallback for a
    // platform that leaves the id out. No fallback to another variant: it would show the wrong
    // colour, size and stock under this line.
    private fun Product.variantFor(line: BagLine): Variant? =
        variants.firstOrNull { line.variantId != null && it.id == line.variantId }
            ?: variants.firstOrNull { line.sku != null && it.sku == line.sku }

    private fun BagLine.toBagLineUi(
        product: Product?,
        variant: Variant?,
        onProductClick: ClickEventOneArg<String>
    ) = BagLineUi(
        id = id,
        line = this,
        notice = toNotice(product = product, variant = variant),
        productCardData = ProductCardType.Horizontal(
            image = when {
                product != null && variant != null -> product.cardImageFor(variant)
                else -> image.toImageUI()
            },
            brand = product?.brandName.orEmpty(),
            name = product?.name ?: name.orEmpty(),
            price = toPrice(variant).toPriceType(),
            color = variant?.colorOptionValue.orEmpty(),
            size = variant?.sizeOptionValue.orEmpty(),
            reference = sku ?: variant?.sku.orEmpty(),
            quantity = quantity,
            // Without the product, stock is unknown: the line stays buyable rather than dimmed.
            isAvailable = product == null || variant?.available == true,
            onClick = { slug?.let(onProductClick) }
        )
    )

    // The Cart's unit price is what the shopper pays. The product's was-price only strikes through
    // when it is still above that price, so an ended sale never shows a "saving".
    private fun BagLine.toPrice(variant: Variant?): Price = Price(
        amount = unitPrice,
        was = variant?.price?.was?.takeIf { was ->
            was.currencyCode == unitPrice.currencyCode && was.amount > unitPrice.amount
        }
    )

    // Stock is read against the quantity the Bag actually holds, not against the threshold alone:
    // three units of a variant with two left is a different problem from a shelf running low.
    private fun BagLine.toNotice(product: Product?, variant: Variant?): BagItemNotice? = when {
        product == null -> null
        variant == null || variant.availableQuantity <= 0 -> BagItemNotice.Unavailable
        quantity > variant.availableQuantity -> BagItemNotice.ExceedsStock(remaining = variant.availableQuantity)
        variant.availableQuantity <= LOW_STOCK_THRESHOLD -> BagItemNotice.LowStock(remaining = variant.availableQuantity)
        else -> null
    }
}
