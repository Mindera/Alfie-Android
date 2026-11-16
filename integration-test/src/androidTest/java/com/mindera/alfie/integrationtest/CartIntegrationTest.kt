package com.mindera.alfie.integrationtest

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Optional
import com.mindera.alfie.graphql.bff.AddToCartMutation
import com.mindera.alfie.graphql.bff.CartQuery
import com.mindera.alfie.graphql.bff.CreateCartMutation
import com.mindera.alfie.graphql.bff.RemoveFromCartMutation
import com.mindera.alfie.graphql.bff.type.CartLineInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * The Cart operations the Bag uses, against the **Mock BFF**: create with a line, add the same
 * variant again, read it back, remove the line.
 */
internal class CartIntegrationTest : BffIntegrationTest() {

    @Test
    fun cart_createAddReadRemove_followsTheBagRules() = withBff { client ->
        val line = CartLineInput(variantId = BARCODE_VARIANT_ID, quantity = Optional.present(1))

        val created = client.executeOrFail(CreateCartMutation(lines = listOf(line))).createCart.cartFragment
        assertEquals(1, created.lineItems.size, "createCart should hold the line it was given")
        val cartId = created.id

        // Adding a variant already in the Cart merges into its line rather than adding a second one.
        val added = client.executeOrFail(AddToCartMutation(cartId = cartId, lines = listOf(line))).addToCart.cartFragment
        assertEquals(1, added.lineItems.size, "Re-adding the variant should merge into its line")
        val merged = added.lineItems.single()
        assertEquals(2, merged.quantity)
        assertEquals(BARCODE_VARIANT_ID, merged.variantId)
        assertTrue(!merged.slug.isNullOrBlank(), "The line should carry the slug the Bag opens the PDP with")

        val read = client.executeOrFail(CartQuery(cartId = cartId)).cart.cartFragment
        assertEquals(added, read, "Reading the Cart should return what the last write answered with")

        val removed = client.executeOrFail(RemoveFromCartMutation(cartId = cartId, lineId = merged.id)).removeFromCart.cartFragment
        assertTrue(removed.lineItems.isEmpty(), "Removing the only line should leave an empty Cart")
        assertEquals(0.0, removed.totals.subtotal.moneyFragment.amount)
    }

    private suspend fun <D : Mutation.Data> ApolloClient.executeOrFail(mutation: Mutation<D>): D =
        mutation(mutation).execute().dataAssertNoErrors

    private companion object {
        /**
         * The Mock BFF's `barcode.variantId` seed anchor: always in stock. Hand-written until the
         * generated `SeedAnchors` lands (#102).
         */
        const val BARCODE_VARIANT_ID = "mock-variant-alfie-oxford-shirt-white-xs"
    }
}
