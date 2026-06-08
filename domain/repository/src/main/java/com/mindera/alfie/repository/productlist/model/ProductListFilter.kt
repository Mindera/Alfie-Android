package com.mindera.alfie.repository.productlist.model

data class ProductListFilter(
    val brandNames: List<String>? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val productTypes: List<String>? = null,
    // TODO: Clarify with the team how currency should be determined for price filtering.
    //  Currently hardcoded as "USD" placeholder — the BFF does not expose a collection-level
    //  currency; it is only available per-product inside priceRange.currencyCode.
    val currencyCode: String = "USD"
)
