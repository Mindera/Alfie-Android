package com.mindera.alfie.data.bag.service

import com.apollographql.apollo.ApolloClient
import com.mindera.alfie.graphql.bff.AddToCartMutation
import com.mindera.alfie.graphql.bff.CartQuery
import com.mindera.alfie.graphql.bff.CreateCartMutation
import com.mindera.alfie.graphql.bff.RemoveFromCartMutation
import com.mindera.alfie.graphql.bff.fragment.CartFragment
import com.mindera.alfie.graphql.bff.type.CartLineInput
import com.mindera.alfie.network.extension.unwrap
import com.mindera.alfie.network.graphql.GraphService
import javax.inject.Inject

internal class CartServiceImpl @Inject constructor(
    apolloClient: ApolloClient
) : GraphService(apolloClient), CartService {

    override suspend fun getCart(cartId: String): Result<CartFragment> =
        query(CartQuery(cartId = cartId)).unwrap().map { it.cart.cartFragment }

    override suspend fun createCart(lines: List<CartLineInput>): Result<CartFragment> =
        mutate(CreateCartMutation(lines = lines)).unwrap().map { it.createCart.cartFragment }

    override suspend fun addToCart(cartId: String, lines: List<CartLineInput>): Result<CartFragment> =
        mutate(AddToCartMutation(cartId = cartId, lines = lines)).unwrap().map { it.addToCart.cartFragment }

    override suspend fun removeFromCart(cartId: String, lineId: String): Result<CartFragment> =
        mutate(RemoveFromCartMutation(cartId = cartId, lineId = lineId)).unwrap().map { it.removeFromCart.cartFragment }
}
