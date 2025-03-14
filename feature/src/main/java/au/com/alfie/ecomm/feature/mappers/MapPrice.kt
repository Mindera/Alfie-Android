package au.com.alfie.ecomm.feature.mappers

import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.product.model.PriceRange

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