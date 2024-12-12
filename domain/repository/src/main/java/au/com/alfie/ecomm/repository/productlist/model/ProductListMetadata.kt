package au.com.alfie.ecomm.repository.productlist.model

import au.com.alfie.ecomm.repository.shared.model.HierarchyItem

data class ProductListMetadata(
    val title: String,
    val totalResults: Int,
    val hierarchy: List<HierarchyItem>
)
