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
     * Units the BFF reports in stock for this variant. Stored as the count rather than a boolean so
     * callers that need to know how many are left can, with [available] derived from it.
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
