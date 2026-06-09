package com.mindera.alfie.data.shared.mapper

import com.mindera.alfie.graphql.fragment.MoneyInfo
import com.mindera.alfie.graphql.fragment.PriceInfo
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.shared.model.Money

internal fun PriceInfo.toDomain() = Price(
    amount = amount.moneyInfo.toDomain(),
    was = was?.moneyInfo?.toDomain()
)

private fun MoneyInfo.toDomain() = Money(
    currencyCode = currencyCode,
    amount = amount.toDouble() / MINOR_UNITS_PER_MAJOR,
    amountFormatted = amountFormatted
)

private const val MINOR_UNITS_PER_MAJOR = 100.0
