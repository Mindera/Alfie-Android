package com.mindera.alfie.data.datastore.cart

/**
 * Stores the device's Cart id. Unencrypted on purpose: see `docs/adr/0001-the-bag-lives-on-the-bff-cart.md`.
 */
interface CartSessionDataSource {

    /** The stored Cart id, or null when the device has no Cart yet. */
    suspend fun getCartId(): String?

    suspend fun setCartId(cartId: String)
}
