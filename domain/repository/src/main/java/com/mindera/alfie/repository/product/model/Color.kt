package com.mindera.alfie.repository.product.model

import com.mindera.alfie.repository.shared.model.Media

data class Color(
    val id: String,
    val name: String?,
    val swatch: Media.Image?,
    val media: List<Media>?
)
