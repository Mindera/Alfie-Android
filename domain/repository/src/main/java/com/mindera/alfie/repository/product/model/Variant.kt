package com.mindera.alfie.repository.product.model

import com.mindera.alfie.repository.shared.model.Attribute
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Size

data class Variant(
    val attributes: List<Attribute>,
    val color: Color?,
    val media: Media.Image,
    val price: Price,
    val size: Size?,
    val sku: String,
    val stock: Int
)
