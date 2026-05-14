package com.mindera.alfie.repository.product.model

import com.mindera.alfie.repository.shared.model.Money

data class PriceRange(
    // The highest price if not a 'from' range.
    val high: Money?,
    // The lowest price.
    val low: Money
)
