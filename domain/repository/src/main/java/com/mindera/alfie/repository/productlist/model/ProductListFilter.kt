package com.mindera.alfie.repository.productlist.model

data class ProductListFilter(
    val brandNames: List<String>? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val productTypes: List<String>? = null,
    val currencyCode: String = "AUD"
)
