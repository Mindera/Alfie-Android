package com.mindera.alfie.repository.productlist.model

data class ProductListFilter(
    val brandNames: List<String>? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val productTypes: List<String>? = null,
    // TODO: derive currencyCode from product price metadata once the BFF exposes it;
    //  hardcoded as "USD" placeholder until then (same pattern as COLLECTION_HANDLE).
    val currencyCode: String = "USD"
)
