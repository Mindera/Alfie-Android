package com.mindera.alfie.repository.productlist.model

import com.mindera.alfie.repository.shared.model.Media

data class ProductListEntry(
    val id: String,
    val slug: String,
    val name: String,
    val brandName: String?,
    val productType: String?,
    val primaryImage: Media.Image?,
    val priceRange: ProductListPriceRange,
    val inventoryTotal: Int?,
    val tags: List<String>
)
