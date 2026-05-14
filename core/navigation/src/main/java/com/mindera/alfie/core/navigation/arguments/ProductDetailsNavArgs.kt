package com.mindera.alfie.core.navigation.arguments

fun productDetailsNavArgs(
    id: String
): ProductDetailsNavArgs = ProductDetailsNavArgs(id = id)

data class ProductDetailsNavArgs(
    val id: String
)
