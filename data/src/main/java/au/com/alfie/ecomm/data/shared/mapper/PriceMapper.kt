package au.com.alfie.ecomm.data.shared.mapper

import au.com.alfie.ecomm.graphql.fragment.MoneyInfo
import au.com.alfie.ecomm.graphql.fragment.PriceInfo
import au.com.alfie.ecomm.graphql.fragment.PriceRangeInfo
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.product.model.PriceRange
import au.com.alfie.ecomm.repository.shared.model.Money

internal fun PriceInfo.toDomain() = Price(
    amount = amount.moneyInfo.toDomain(),
    was = was?.moneyInfo?.toDomain()
)

internal fun PriceRangeInfo.toDomain() = PriceRange(
    high = high?.moneyInfo?.toDomain(),
    low = low.moneyInfo.toDomain()
)

private fun MoneyInfo.toDomain() = Money(
    currencyCode = currencyCode,
    amount = amount,
    amountFormatted = amountFormatted
)
