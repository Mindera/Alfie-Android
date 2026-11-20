package com.mindera.alfie.data.bag.service

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.exception.DefaultApolloException
import com.mindera.alfie.core.test.setPrivatePropertyField
import com.mindera.alfie.graphql.bff.AddToCartMutation
import com.mindera.alfie.graphql.bff.CartQuery
import com.mindera.alfie.graphql.bff.CreateCartMutation
import com.mindera.alfie.graphql.bff.RemoveFromCartMutation
import com.mindera.alfie.graphql.bff.fragment.CartFragment
import com.mindera.alfie.graphql.bff.fragment.MoneyFragment
import com.mindera.alfie.graphql.bff.type.CartLineInput
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
internal class CartServiceImplTest {

    @RelaxedMockK
    private lateinit var apolloClient: ApolloClient

    @InjectMockKs
    private lateinit var service: CartServiceImpl

    private val cart = CartFragment(
        id = "cart-1",
        lineItems = emptyList(),
        totals = CartFragment.Totals(
            subtotal = CartFragment.Subtotal(
                __typename = "Money",
                moneyFragment = MoneyFragment(amount = 0.0, currencyCode = "GBP")
            )
        )
    )

    private val lines = listOf(
        CartLineInput(variantId = "v-1", productId = Optional.present("p-1"), quantity = Optional.present(1))
    )

    @Test
    fun `getCart - WHEN the query succeeds THEN returns the Cart`() = runTest {
        every { apolloClient.query(CartQuery(cartId = "cart-1")) } returns
            call(success(CartQuery.Data(cart = CartQuery.Cart(__typename = "Cart", cartFragment = cart))))

        assertEquals(Result.success(cart), service.getCart("cart-1"))
    }

    @Test
    fun `getCart - WHEN the response has errors THEN returns failure`() = runTest {
        every { apolloClient.query(CartQuery(cartId = "cart-1")) } returns call(failure<CartQuery.Data>())

        assertTrue(service.getCart("cart-1").isFailure)
    }

    @Test
    fun `createCart - sends the lines and returns the new Cart`() = runTest {
        every { apolloClient.mutation(CreateCartMutation(lines = lines)) } returns call(
            success(CreateCartMutation.Data(createCart = CreateCartMutation.CreateCart(__typename = "Cart", cartFragment = cart)))
        )

        assertEquals(Result.success(cart), service.createCart(lines))
    }

    @Test
    fun `addToCart - sends the Cart id and lines and returns the Cart`() = runTest {
        every { apolloClient.mutation(AddToCartMutation(cartId = "cart-1", lines = lines)) } returns call(
            success(AddToCartMutation.Data(addToCart = AddToCartMutation.AddToCart(__typename = "Cart", cartFragment = cart)))
        )

        assertEquals(Result.success(cart), service.addToCart(cartId = "cart-1", lines = lines))
    }

    @Test
    fun `addToCart - WHEN the response has errors THEN returns failure`() = runTest {
        every { apolloClient.mutation(AddToCartMutation(cartId = "cart-1", lines = lines)) } returns
            call(failure<AddToCartMutation.Data>())

        assertTrue(service.addToCart(cartId = "cart-1", lines = lines).isFailure)
    }

    @Test
    fun `removeFromCart - sends the Cart id and line id and returns the Cart`() = runTest {
        every { apolloClient.mutation(RemoveFromCartMutation(cartId = "cart-1", lineId = "line-1")) } returns call(
            success(
                RemoveFromCartMutation.Data(
                    removeFromCart = RemoveFromCartMutation.RemoveFromCart(__typename = "Cart", cartFragment = cart)
                )
            )
        )

        assertEquals(Result.success(cart), service.removeFromCart(cartId = "cart-1", lineId = "line-1"))
    }

    private fun <D : Operation.Data> call(response: ApolloResponse<D>): ApolloCall<D> {
        val call = mockk<ApolloCall<D>>()
        coEvery { call.execute() } returns response
        return call
    }

    private fun <D : Operation.Data> success(data: D): ApolloResponse<D> {
        val response = mockk<ApolloResponse<D>>()
        response.setPrivatePropertyField("data", data)
        every { response.hasErrors() } returns false
        every { response.dataAssertNoErrors } returns data
        return response
    }

    private fun <D : Operation.Data> failure(): ApolloResponse<D> {
        val response = mockk<ApolloResponse<D>>()
        response.setPrivatePropertyField("exception", DefaultApolloException("Error message"))
        every { response.hasErrors() } returns true
        return response
    }
}
