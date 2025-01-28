package au.com.alfie.ecomm.feature.mappers

import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.product.model.PriceRange

fun mapPrice(
    priceRange: PriceRange?,
    price: Price
): PriceType {
    val rangeHigh = priceRange?.high
    val salePrice = price.was
    return when {
        rangeHigh != null -> PriceType.Range(
            startPrice = priceRange.low.amountFormatted,
            endPrice = rangeHigh.amountFormatted
        )

        salePrice != null -> PriceType.Sale(
            fullPrice = salePrice.amountFormatted,
            salePrice = price.amount.amountFormatted
        )

        else -> PriceType.Default(price = price.amount.amountFormatted)
    }
}