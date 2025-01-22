package au.com.alfie.ecomm.repository.product.model

import au.com.alfie.ecomm.repository.shared.model.Media

data class Color(
    val id: String,
    val name: String?,
    val swatch: Media.Image?,
    val media: List<Media?>?
)
