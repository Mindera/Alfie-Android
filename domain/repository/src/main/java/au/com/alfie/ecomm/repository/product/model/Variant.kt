package au.com.alfie.ecomm.repository.product.model

import au.com.alfie.ecomm.repository.shared.model.Attribute
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.Size

data class Variant(
    val attributes: List<Attribute>,
    val color: Color?,
    val media: Media.Image,
    val price: Price,
    val size: Size?,
    val sku: String,
    val stock: Int
)
