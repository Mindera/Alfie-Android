package au.com.alfie.ecomm.repository.product.model

import au.com.alfie.ecomm.repository.shared.model.Attribute
import au.com.alfie.ecomm.repository.shared.model.Brand
import au.com.alfie.ecomm.repository.shared.model.HierarchyItem
import au.com.alfie.ecomm.repository.shared.model.Size

data class Product(
    val id: String,
    val attributes: List<Attribute>,
    val brand: Brand,
    val defaultVariant: Variant,
    val hierarchy: List<HierarchyItem>,
    val labels: List<String>,
    val longDescription: String?,
    val name: String,
    val priceRange: PriceRange?,
    val shortDescription: String,
    val sizes: List<Size>,
    val slug: String,
    val styleNumber: String,
    val variants: List<Variant>
)
