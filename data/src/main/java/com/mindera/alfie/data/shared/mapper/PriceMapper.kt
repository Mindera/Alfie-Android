package com.mindera.alfie.data.shared.mapper

import com.mindera.alfie.graphql.fragment.MoneyInfo
import com.mindera.alfie.graphql.fragment.PriceInfo
import com.mindera.alfie.graphql.fragment.PriceRangeInfo
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.PriceRange
import com.mindera.alfie.repository.shared.model.Money

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
