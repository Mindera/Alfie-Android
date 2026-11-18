package com.mindera.alfie.core.navigation.arguments

fun productDetailsNavArgs(
    handle: String,
    variantId: String? = null
): ProductDetailsNavArgs = ProductDetailsNavArgs(
    handle = handle,
    variantId = variantId
)

data class ProductDetailsNavArgs(
    val handle: String,
    /**
     * The variant to open the PDP on, when the caller already knows which one the shopper means:
     * a barcode scan resolves to one specific size, and a product deep link can name one with
     * `?variantId=` (the BFF's `ProductVariant.id`). Making them pick it again would be asking
     * for information they already supplied. Null everywhere else, and an id that matches none of
     * the product's variants is treated the same: the PDP then picks its own default variant.
     *
     * No default here on purpose — Compose Destinations cannot read one across module boundaries,
     * so [productDetailsNavArgs] carries it instead, as [WebViewNavArgs] already does.
     */
    val variantId: String?
)
