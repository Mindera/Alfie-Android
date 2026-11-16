package com.mindera.alfie.data.bag.service

import com.mindera.alfie.graphql.bff.fragment.CartFragment
import com.mindera.alfie.graphql.bff.type.CartLineInput

internal interface CartService {

    suspend fun getCart(cartId: String): Result<CartFragment>

    suspend fun createCart(lines: List<CartLineInput>): Result<CartFragment>

    suspend fun addToCart(cartId: String, lines: List<CartLineInput>): Result<CartFragment>

    suspend fun removeFromCart(cartId: String, lineId: String): Result<CartFragment>
}
