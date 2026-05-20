package com.mindera.alfie.repository.productlist.model

import com.mindera.alfie.repository.product.model.Color
import com.mindera.alfie.repository.product.model.PriceRange
import com.mindera.alfie.repository.shared.model.Attribute
import com.mindera.alfie.repository.shared.model.Brand

data class ProductListEntry(
    val id: String,
    val styleNumber: String,
    val name: String,
    val brand: Brand,
    val priceRange: PriceRange?,
    val shortDescription: String,
    val longDescription: String?,
    val slug: String,
    val labels: List<String>,
    val attributes: List<Attribute>,
    val defaultVariant: ProductListEntryVariant,
    val variants: List<ProductListEntryVariant>,
    val colors: List<Color>
)
