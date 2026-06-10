package com.mindera.alfie.core.navigation.arguments

fun productDetailsNavArgs(
    handle: String
): ProductDetailsNavArgs = ProductDetailsNavArgs(handle = handle)

data class ProductDetailsNavArgs(
    val handle: String
)
