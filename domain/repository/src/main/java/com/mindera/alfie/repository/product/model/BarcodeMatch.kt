package com.mindera.alfie.repository.product.model

/**
 * The product a scanned barcode resolves to, as returned by the BFF's `productByBarcode` query.
 *
 * Deliberately thinner than [Product]: a scan only needs enough to open the PDP, and the BFF
 * resolves a barcode through a product search rather than a full product read.
 */
data class BarcodeMatch(
    val id: String,
    val name: String,
    /** The handle the PDP is opened with. */
    val slug: String,
    /**
     * The variant carrying the scanned code, or null when no single variant does — the BFF
     * returns null both when no variant holds the code and when more than one does.
     */
    val variantId: String?
)
