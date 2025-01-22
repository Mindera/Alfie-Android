package au.com.alfie.ecomm.repository.productlist.model

import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.Size

data class ProductListEntryVariant(
    val color: String?,
    val media: Media.Image?,
    val price: Price,
    val size: Size?,
    val stock: Int
)
