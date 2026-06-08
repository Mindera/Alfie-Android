package com.mindera.alfie.repository.productlist.model

data class ProductList(
    val products: List<ProductListEntry>,
    val pagination: CursorPagination
)
