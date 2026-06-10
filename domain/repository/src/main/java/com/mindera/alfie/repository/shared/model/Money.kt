package com.mindera.alfie.repository.shared.model

data class Money(
    val amount: Double,
    val amountFormatted: String,
    val currencyCode: String
)
