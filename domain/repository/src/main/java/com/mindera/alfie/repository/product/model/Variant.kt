package com.mindera.alfie.repository.product.model

import com.mindera.alfie.repository.shared.model.Media

data class Variant(
    val id: String,
    val sku: String,
    val price: Price,
    val options: List<VariantOption>,
    val media: List<Media.Image>,
    val available: Boolean
)
