package au.com.alfie.ecomm.data.shared

import au.com.alfie.ecomm.graphql.fragment.MoneyInfo
import au.com.alfie.ecomm.graphql.fragment.PriceInfo
import au.com.alfie.ecomm.graphql.fragment.PriceRangeInfo
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.product.model.PriceRange
import au.com.alfie.ecomm.repository.shared.model.Money

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
