package com.mindera.alfie.repository.productlist.model

data class ProductListPriceRange(
    val minAmount: Double,
    val maxAmount: Double,
    val currencyCode: String
)
