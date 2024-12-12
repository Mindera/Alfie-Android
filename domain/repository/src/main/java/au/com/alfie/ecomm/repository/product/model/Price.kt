package au.com.alfie.ecomm.repository.product.model

import au.com.alfie.ecomm.repository.shared.model.Money

data class Price(
    val amount: Money,
    val was: Money?
)
