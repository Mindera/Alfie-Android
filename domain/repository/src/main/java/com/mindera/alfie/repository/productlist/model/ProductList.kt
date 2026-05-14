package com.mindera.alfie.repository.productlist.model

import com.mindera.alfie.repository.shared.model.Pagination

data class ProductList(
    val title: String,
    val products: List<ProductListEntry>,
    val pagination: Pagination
)
