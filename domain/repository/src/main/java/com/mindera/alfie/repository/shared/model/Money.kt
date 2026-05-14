package com.mindera.alfie.repository.shared.model

data class Money(
    val amount: Int,
    val amountFormatted: String,
    val currencyCode: String
)
