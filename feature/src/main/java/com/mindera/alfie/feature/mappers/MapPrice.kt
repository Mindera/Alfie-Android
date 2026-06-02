package com.mindera.alfie.feature.mappers

import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.PriceRange
import com.mindera.alfie.repository.productlist.model.ProductListPriceRange
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Price.toPriceType(): PriceType {
    val previousPrice = this.was
    return if (previousPrice != null) {
        PriceType.Sale(
            fullPrice = previousPrice.amountFormatted,
            salePrice = amount.amountFormatted
        )
    } else {
        PriceType.Default(price = amount.amountFormatted)
    }
}

fun PriceRange?.toPriceType(default: Price): PriceType {
    val rangeHigh = this?.high
    val rangeLow = this?.low
    return if (rangeHigh != null && rangeLow != null) {
        PriceType.Range(
            startPrice = rangeLow.amountFormatted,
            endPrice = rangeHigh.amountFormatted
        )
    } else {
        PriceType.Default(price = default.amount.amountFormatted)
    }
}

fun ProductListPriceRange.toPriceType(): PriceType {
    val minFormatted = formatAsMoney(minAmount, currencyCode)
    val maxFormatted = formatAsMoney(maxAmount, currencyCode)
    return if (minAmount != maxAmount) {
        PriceType.Range(startPrice = minFormatted, endPrice = maxFormatted)
    } else {
        PriceType.Default(price = minFormatted)
    }
}

private fun formatAsMoney(amount: Double, currencyCode: String): String = runCatching {
    val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
    format.currency = Currency.getInstance(currencyCode)
    format.format(amount)
}.getOrElse { "%.2f".format(amount) }