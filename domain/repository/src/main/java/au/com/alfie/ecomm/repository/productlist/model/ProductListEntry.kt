package au.com.alfie.ecomm.repository.productlist.model

import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.product.model.PriceRange
import au.com.alfie.ecomm.repository.shared.model.Brand
import au.com.alfie.ecomm.repository.shared.model.Size

data class ProductListEntry(
    val brand: Brand,
    val colors: List<Color>,
    val id: String,
    val labels: List<String>,
    val name: String,
    val priceRange: PriceRange?,
    val shortDescription: String,
    val sizes: List<Size>,
    val slug: String,
    val styleNumber: String,
    val defaultVariant: ProductListEntryVariant
)
