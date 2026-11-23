package com.mindera.alfie.data.bag

import app.cash.turbine.test
import com.apollographql.apollo.api.Optional
import com.mindera.alfie.data.bag.service.CartService
import com.mindera.alfie.data.datastore.cart.CartSessionDataSource
import com.mindera.alfie.graphql.bff.fragment.CartFragment
import com.mindera.alfie.graphql.bff.fragment.MoneyFragment
import com.mindera.alfie.graphql.bff.type.CartLineInput
import com.mindera.alfie.network.exception.GraphNetworkException
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.result.ErrorType
import com.mindera.alfie.repository.result.RepositoryResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class BagRepositoryImplTest {

    private val cartService = FakeCartService()
    private val cartSession = FakeCartSession()
    private val subject = BagRepositoryImpl(cartService = cartService, cartSession = cartSession)

    @Test
    fun `getBag - WHEN the device has no Cart THEN emits the empty Bag without calling the BFF`() = runTest {
        subject.getBag().test {
            assertEquals(RepositoryResult.Success(Bag.EMPTY), awaitItem())
        }
        assertEquals(0, cartService.getCartCalls)
    }

    @Test
    fun `getBag - WHEN the device has a Cart THEN emits that Cart's Bag`() = runTest {
        cartSession.cartId = "cart-1"
        cartService.cart = cart(id = "cart-1", line(id = "line-1", variantId = "v1", quantity = 2))

        subject.getBag().test {
            val bag = assertIs<RepositoryResult.Success<Bag>>(awaitItem()).data
            assertEquals(listOf("line-1"), bag.lines.map { it.id })
            assertEquals(2, bag.lines.single().quantity)
        }
    }

    @Test
    fun `addToBag - WHEN the device has no Cart THEN creates one with the line and stores its id`() = runTest {
        cartService.cart = cart(id = "cart-new", line(id = "line-1", variantId = "v1"))

        val result = subject.addToBag(variantId = "v1", productId = "p1", quantity = 1)

        assertIs<RepositoryResult.Success<Bag>>(result)
        assertEquals(1, cartService.createCartCalls)
        assertEquals(0, cartService.addToCartCalls)
        assertEquals("cart-new", cartSession.cartId)
        assertEquals(
            listOf(CartLineInput(variantId = "v1", productId = Optional.present("p1"), quantity = Optional.present(1))),
            cartService.lastLines
        )
    }

    @Test
    fun `addToBag - WHEN the device has a Cart THEN appends to it`() = runTest {
        cartSession.cartId = "cart-1"
        cartService.cart = cart(id = "cart-1", line(id = "line-1", variantId = "v1"))

        subject.addToBag(variantId = "v1", productId = null, quantity = 1)

        assertEquals(0, cartService.createCartCalls)
        assertEquals(listOf("cart-1"), cartService.addToCartIds)
    }

    @Test
    fun `addToBag - WHEN two first adds overlap THEN only one Cart is created`() = runTest {
        cartService.cart = cart(id = "cart-new", line(id = "line-1", variantId = "v1"))
        cartService.delayMillis = 100

        listOf(
            async { subject.addToBag(variantId = "v1", productId = null, quantity = 1) },
            async { subject.addToBag(variantId = "v2", productId = null, quantity = 1) }
        ).awaitAll()

        assertEquals(1, cartService.createCartCalls)
        assertEquals(listOf("cart-new"), cartService.addToCartIds)
    }

    @Test
    fun `addToBag - WHEN it succeeds THEN getBag emits the Cart the BFF answered with`() = runTest {
        cartService.cart = cart(id = "cart-new", line(id = "line-1", variantId = "v1", quantity = 3))

        subject.getBag().test {
            assertEquals(RepositoryResult.Success(Bag.EMPTY), awaitItem())

            subject.addToBag(variantId = "v1", productId = null, quantity = 3)

            val bag = assertIs<RepositoryResult.Success<Bag>>(awaitItem()).data
            assertEquals(3, bag.lines.single().quantity)
        }
    }

    @Test
    fun `addToBag - WHEN it fails THEN returns the error and keeps the Bag as it was`() = runTest {
        cartService.failure = GraphNetworkException.ServerException(code = 500, message = "boom")

        subject.getBag().test {
            assertEquals(RepositoryResult.Success(Bag.EMPTY), awaitItem())

            val result = subject.addToBag(variantId = "v1", productId = null, quantity = 1)

            assertEquals(ErrorType.SERVER_ERROR, assertIs<RepositoryResult.Error>(result).errorResult.type)
            expectNoEvents()
        }
        assertEquals(null, cartSession.cartId)
    }

    @Test
    fun `removeFromBag - WHEN the device has a Cart THEN removes the line and emits the new Bag`() = runTest {
        cartSession.cartId = "cart-1"
        cartService.cart = cart(id = "cart-1")

        val result = subject.removeFromBag("line-1")

        assertEquals(RepositoryResult.Success(Bag(lines = emptyList(), subtotal = gbp(0.0).let(::money))), result)
        assertEquals(listOf("cart-1" to "line-1"), cartService.removeCalls)
    }

    @Test
    fun `removeFromBag - WHEN the device has no Cart THEN fails without calling the BFF`() = runTest {
        val result = subject.removeFromBag("line-1")

        assertEquals(ErrorType.RESOURCE_NOT_FOUND, assertIs<RepositoryResult.Error>(result).errorResult.type)
        assertEquals(emptyList(), cartService.removeCalls)
    }

    @Test
    fun `refreshBag - WHEN the fetch fails THEN getBag emits the error`() = runTest {
        cartSession.cartId = "cart-1"
        cartService.failure = GraphNetworkException.NetworkException(code = 1000, message = "offline")

        subject.getBag().test {
            assertIs<RepositoryResult.Error>(awaitItem())
        }
    }

    private fun gbp(amount: Double) = MoneyFragment(amount = amount, currencyCode = "GBP")

    private fun money(fragment: MoneyFragment) = com.mindera.alfie.repository.shared.model.Money(
        amount = fragment.amount,
        amountFormatted = com.mindera.alfie.core.commons.string.formatMoney(fragment.amount, fragment.currencyCode),
        currencyCode = fragment.currencyCode
    )

    private fun line(id: String, variantId: String, quantity: Int = 1) = CartFragment.LineItem(
        id = id,
        productId = "p-$variantId",
        variantId = variantId,
        sku = "sku-$variantId",
        slug = "slug-$variantId",
        name = "Name $variantId",
        quantity = quantity,
        image = null,
        price = CartFragment.Price(__typename = "Money", moneyFragment = gbp(10.0)),
        lineTotal = CartFragment.LineTotal(__typename = "Money", moneyFragment = gbp(10.0 * quantity))
    )

    private fun cart(id: String, vararg lines: CartFragment.LineItem) = CartFragment(
        id = id,
        lineItems = lines.toList(),
        totals = CartFragment.Totals(
            subtotal = CartFragment.Subtotal(
                __typename = "Money",
                moneyFragment = gbp(lines.sumOf { it.lineTotal.moneyFragment.amount })
            )
        )
    )

    private class FakeCartSession : CartSessionDataSource {
        var cartId: String? = null

        override suspend fun getCartId(): String? = cartId

        override suspend fun setCartId(cartId: String) {
            this.cartId = cartId
        }
    }

    private class FakeCartService : CartService {
        lateinit var cart: CartFragment
        var failure: Throwable? = null
        var delayMillis = 0L

        var getCartCalls = 0
        var createCartCalls = 0
        val addToCartIds = mutableListOf<String>()
        val addToCartCalls get() = addToCartIds.size
        val removeCalls = mutableListOf<Pair<String, String>>()
        var lastLines: List<CartLineInput> = emptyList()

        override suspend fun getCart(cartId: String): Result<CartFragment> {
            getCartCalls++
            return answer()
        }

        override suspend fun createCart(lines: List<CartLineInput>): Result<CartFragment> {
            createCartCalls++
            lastLines = lines
            return answer()
        }

        override suspend fun addToCart(cartId: String, lines: List<CartLineInput>): Result<CartFragment> {
            addToCartIds += cartId
            lastLines = lines
            return answer()
        }

        override suspend fun removeFromCart(cartId: String, lineId: String): Result<CartFragment> {
            removeCalls += cartId to lineId
            return answer()
        }

        private suspend fun answer(): Result<CartFragment> {
            delay(delayMillis)
            return failure?.let { Result.failure(it) } ?: Result.success(cart)
        }
    }
}
