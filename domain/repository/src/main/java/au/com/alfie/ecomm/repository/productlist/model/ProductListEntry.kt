package au.com.alfie.ecomm.repository.productlist.model

import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.product.model.PriceRange
import au.com.alfie.ecomm.repository.shared.model.Attribute
import au.com.alfie.ecomm.repository.shared.model.Brand

data class ProductListEntry(
    val id: String,
    val styleNumber: String,
    val name: String,
    val brand: Brand,
    val priceRange: PriceRange?,
    val shortDescription: String,
    val longDescription: String?,
    val slug: String,
    val labels: List<String>,
    val attributes: List<Attribute>,
    val defaultVariant: ProductListEntryVariant,
    val variants: List<ProductListEntryVariant>,
    val colors: List<Color>,
)
