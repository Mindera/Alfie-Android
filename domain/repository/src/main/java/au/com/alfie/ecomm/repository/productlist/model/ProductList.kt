package au.com.alfie.ecomm.repository.productlist.model

import au.com.alfie.ecomm.repository.shared.model.Pagination

data class ProductList(
    val title: String,
    val products: List<ProductListEntry>,
    val pagination: Pagination
)
