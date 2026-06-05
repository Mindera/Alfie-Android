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
 * Formats a money amount as a display string using [currencySymbol] and a truncated integer part.
 * Example: `formatMoney(199.99, "USD") == "$199"`.
 */
fun formatMoney(amount: Double, currencyCode: String): String =
    "${currencySymbol(currencyCode)}${amount.toInt()}"
