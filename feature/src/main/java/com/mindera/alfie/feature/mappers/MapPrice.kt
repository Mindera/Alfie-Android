package com.mindera.alfie.feature.mappers

import com.mindera.alfie.core.commons.string.formatMoney
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.PriceRange
import com.mindera.alfie.repository.productlist.model.ProductListPriceRange

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

fun PriceRange?.toPriceType(default: Price?): PriceType {
    val rangeHigh = this?.high
    val rangeLow = this?.low
    return if (rangeHigh != null && rangeLow != null) {
        PriceType.Range(
            startPrice = rangeLow.amountFormatted,
            endPrice = rangeHigh.amountFormatted
        )
    } else {
        PriceType.Default(price = default?.amount?.amountFormatted.orEmpty())
    }
}

fun ProductListPriceRange.toPriceType(): PriceType {
    val minFormatted = formatMoney(minAmount, currencyCode)
    val maxFormatted = formatMoney(maxAmount, currencyCode)
    return if (minAmount != maxAmount) {
        PriceType.Range(startPrice = minFormatted, endPrice = maxFormatted)
    } else {
        PriceType.Default(price = minFormatted)
    }
}