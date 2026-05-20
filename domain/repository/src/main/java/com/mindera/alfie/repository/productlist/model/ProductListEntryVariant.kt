package com.mindera.alfie.repository.productlist.model

import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Size

data class ProductListEntryVariant(
    val color: String?,
    val media: List<Media>?,
    val price: Price,
    val size: Size?,
    val stock: Int
) {
    val defaultMedia = media?.first { it is Media.Image } as Media.Image
}
