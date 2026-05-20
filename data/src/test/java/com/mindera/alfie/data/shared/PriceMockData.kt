package com.mindera.alfie.data.shared

import com.mindera.alfie.graphql.fragment.MoneyInfo
import com.mindera.alfie.graphql.fragment.PriceInfo
import com.mindera.alfie.graphql.fragment.PriceRangeInfo
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.PriceRange
import com.mindera.alfie.repository.shared.model.Money

internal val priceInfoData = PriceInfo(
    amount = PriceInfo.Amount(
        __typename = "",
        moneyInfo = MoneyInfo(
            currencyCode = "AUD",
            amount = 40000,
            amountFormatted = "$400.00"
        )
    ),
    was = null
)

internal val priceRangeInfoData = PriceRangeInfo(
    high = PriceRangeInfo.High(
        __typename = "",
        moneyInfo = MoneyInfo(
            currencyCode = "AUD",
            amount = 50000,
            amountFormatted = "$500.00"
        )
    ),
    low = PriceRangeInfo.Low(
        __typename = "",
        moneyInfo = MoneyInfo(
            currencyCode = "AUD",
            amount = 40000,
            amountFormatted = "$400.00"
        )
    )
)

internal val price = Price(
    amount = Money(
        currencyCode = "AUD",
        amount = 40000,
        amountFormatted = "$400.00"
    ),
    was = null
)

internal val priceRange = PriceRange(
    high = Money(
        currencyCode = "AUD",
        amount = 50000,
        amountFormatted = "$500.00"
    ),
    low = Money(
        currencyCode = "AUD",
        amount = 40000,
        amountFormatted = "$400.00"
    )
)
