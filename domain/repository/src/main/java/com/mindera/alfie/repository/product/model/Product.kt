package com.mindera.alfie.repository.product.model

import com.mindera.alfie.repository.shared.model.Media

data class Product(
    val id: String,
    val name: String,
    val slug: String,
    val brandName: String?,
    val descriptionHtml: String?,
    val defaultVariantId: String?,
    val images: List<Media.Image>,
    val priceRange: PriceRange?,
    val variants: List<Variant>
)

fun Product.resolveDefaultVariant(): Variant? =
    variants.firstOrNull { it.id == defaultVariantId }
        ?: variants.firstOrNull { it.available }
        ?: variants.firstOrNull()
