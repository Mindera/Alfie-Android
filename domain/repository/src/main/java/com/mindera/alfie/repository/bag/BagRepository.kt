package com.mindera.alfie.repository.bag

import com.mindera.alfie.repository.result.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * The Bag, backed by the BFF Cart. Every write is pessimistic: it returns the Cart the BFF answered
 * with, which replaces the Bag wholesale. See `docs/adr/0001-the-bag-lives-on-the-bff-cart.md`.
 */
interface BagRepository {

    /**
     * The current Bag. The first collection fetches the Cart when the device has one; a device with
     * no Cart has the empty Bag. Every successful write emits the Bag it produced.
     */
    fun getBag(): Flow<RepositoryResult<Bag>>

    /** Fetches the Cart again, e.g. to retry after a failed load. */
    suspend fun refreshBag(): RepositoryResult<Bag>

    /**
     * Adds [quantity] units of the variant. Creates the Cart, with this line, when the device has
     * none. Adding a variant already in the Bag raises its line's quantity.
     *
     * [productId] is optional: BigCommerce needs it and Shopify ignores it.
     */
    suspend fun addToBag(variantId: String, productId: String?, quantity: Int = 1): RepositoryResult<Bag>

    /** Removes the line with this Cart line id, whatever its quantity. */
    suspend fun removeFromBag(lineId: String): RepositoryResult<Bag>
}
