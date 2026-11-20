package com.mindera.alfie.repository.bag

import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money

/**
 * The shopper's Bag, as the BFF Cart holds it. [subtotal] is null only for the empty Bag of a device
 * that has no Cart yet.
 */
data class Bag(
    val lines: List<BagLine>,
    val subtotal: Money?
) {
    companion object {
        val EMPTY = Bag(lines = emptyList(), subtotal = null)
    }
}

/**
 * One variant in the Bag, with its quantity. [id] is the Cart's line id. The product fields are
 * nullable because the BFF's Cart line declares them so; [slug] is what reaches the PDP.
 */
data class BagLine(
    val id: String,
    val productId: String?,
    val variantId: String?,
    val sku: String?,
    val slug: String?,
    val name: String?,
    val image: Media.Image?,
    val quantity: Int,
    val unitPrice: Money,
    val lineTotal: Money
)
