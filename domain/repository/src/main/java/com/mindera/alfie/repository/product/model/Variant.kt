package com.mindera.alfie.repository.product.model

import com.mindera.alfie.repository.shared.model.Media

private const val OPTION_NAME_COLOR = "color"
private const val OPTION_NAME_COLOUR = "colour"
private const val OPTION_NAME_SIZE = "size"

data class Variant(
    val id: String,
    val sku: String,
    val price: Price,
    val options: List<VariantOption>,
    val media: List<Media.Image>,
    /**
     * Units the BFF reports in stock for this variant (`inventory.available`). The Bag screen needs
     * the count itself — not just "in stock" — to decide whether to surface the low-stock message,
     * so this is stored and [available] is derived from it rather than the other way round.
     */
    val availableQuantity: Int
) {
    val available: Boolean get() = availableQuantity > 0
}

/** Value of this variant's colour option ("color"/"colour"), or null when it has none. */
val Variant.colorOptionValue: String?
    get() = options
        .firstOrNull { it.name.equals(other = OPTION_NAME_COLOR, ignoreCase = true) || it.name.equals(other = OPTION_NAME_COLOUR, ignoreCase = true) }
        ?.value

/** Value of this variant's size option, or null when it has none. */
val Variant.sizeOptionValue: String?
    get() = options
        .firstOrNull { it.name.equals(other = OPTION_NAME_SIZE, ignoreCase = true) }
        ?.value
