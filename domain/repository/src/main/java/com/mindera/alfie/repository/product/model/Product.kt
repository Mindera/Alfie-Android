package com.mindera.alfie.repository.product.model

import com.mindera.alfie.repository.shared.model.Attribute
import com.mindera.alfie.repository.shared.model.Brand

data class Product(
    val id: String,
    val attributes: List<Attribute>,
    val brand: Brand,
    val defaultVariant: Variant,
    val labels: List<String>,
    val longDescription: String?,
    val name: String,
    val priceRange: PriceRange?,
    val shortDescription: String,
    val slug: String,
    val styleNumber: String,
    val variants: List<Variant>,
    val colors: List<Color>
)
