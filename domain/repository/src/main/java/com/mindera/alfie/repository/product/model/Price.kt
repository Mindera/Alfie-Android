package com.mindera.alfie.repository.product.model

import com.mindera.alfie.repository.shared.model.Money

data class Price(
    val amount: Money,
    val was: Money?
)
