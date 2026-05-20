package com.mindera.alfie.core.navigation.arguments.productlist

fun productListNavArgs(
    type: ProductListType
) = ProductListNavArgs(
    type = type
)

data class ProductListNavArgs(
    val type: ProductListType
)
