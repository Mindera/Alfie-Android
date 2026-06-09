package com.mindera.alfie.core.commons.string

/**
 * Maps an ISO-4217 currency code to its display symbol.
 * Falls back to "$" for unknown codes.
 */
fun currencySymbol(currencyCode: String): String = when (currencyCode.uppercase()) {
    "USD", "AUD", "NZD", "CAD", "SGD", "HKD" -> "$"
    "GBP" -> "£"
    "EUR" -> "€"
    "JPY", "CNY" -> "¥"
    else -> "$"
}

/**
 * Formats a money [amount] (in major currency units) as a display string by prefixing the
 * [currencySymbol]. Example: `formatMoney(199.99, "USD") == "$199.99"`.
 *
 * Intentionally not locale-aware: the BFF is expected to own currency formatting; this is a
 * client-side stand-in until that contract lands.
 *
 * @param showFractionDigits when `false`, the fractional part is dropped (e.g. `"$199"`),
 * useful for compact contexts such as price-range slider labels.
 */
fun formatMoney(
    amount: Double,
    currencyCode: String,
    showFractionDigits: Boolean = true
): String {
    val symbol = currencySymbol(currencyCode)
    return if (showFractionDigits) "$symbol%.2f".format(amount) else "$symbol${amount.toInt()}"
}
