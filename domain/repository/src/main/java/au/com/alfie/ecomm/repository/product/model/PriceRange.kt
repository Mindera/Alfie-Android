package au.com.alfie.ecomm.repository.product.model

import au.com.alfie.ecomm.repository.shared.model.Money

data class PriceRange(
    // The highest price if not a 'from' range.
    val high: Money?,
    // The lowest price.
    val low: Money
)
