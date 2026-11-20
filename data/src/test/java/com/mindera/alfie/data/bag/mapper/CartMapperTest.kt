package com.mindera.alfie.data.bag.mapper

import com.mindera.alfie.core.commons.string.formatMoney
import com.mindera.alfie.graphql.bff.fragment.CartFragment
import com.mindera.alfie.graphql.bff.fragment.ImageFragment
import com.mindera.alfie.graphql.bff.fragment.MoneyFragment
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.bag.BagLine
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class CartMapperTest {

    @Test
    fun `toDomain - maps every line field and the subtotal`() {
        val cart = cart(
            lineItem(
                image = CartFragment.Image(
                    __typename = "Image",
                    imageFragment = ImageFragment(url = "https://img/shirt.jpg", altText = "Oxford Shirt")
                )
            ),
            subtotal = 90.0
        )

        val expected = Bag(
            lines = listOf(
                BagLine(
                    id = "line-1",
                    productId = "p-1",
                    variantId = "v-1",
                    sku = "sku-1",
                    slug = "oxford-shirt",
                    name = "Oxford Shirt",
                    image = Media.Image(url = "https://img/shirt.jpg", alt = "Oxford Shirt"),
                    quantity = 2,
                    unitPrice = gbpMoney(45.0),
                    lineTotal = gbpMoney(90.0)
                )
            ),
            subtotal = gbpMoney(90.0)
        )
        assertEquals(expected, cart.toDomain())
    }

    @Test
    fun `toDomain - WHEN the line has no image or product fields THEN they map to null`() {
        val cart = cart(
            lineItem(image = null).copy(productId = null, variantId = null, sku = null, slug = null, name = null),
            subtotal = 90.0
        )

        val line = cart.toDomain().lines.single()

        assertNull(line.image)
        assertNull(line.productId)
        assertNull(line.variantId)
        assertNull(line.sku)
        assertNull(line.slug)
        assertNull(line.name)
    }

    @Test
    fun `toDomain - WHEN the Cart has no lines THEN the Bag is empty with a zero subtotal`() {
        val bag = cart(subtotal = 0.0).toDomain()

        assertEquals(emptyList(), bag.lines)
        assertEquals(gbpMoney(0.0), bag.subtotal)
    }

    private fun gbp(amount: Double) = MoneyFragment(amount = amount, currencyCode = "GBP")

    private fun gbpMoney(amount: Double) = Money(
        currencyCode = "GBP",
        amount = amount,
        amountFormatted = formatMoney(amount = amount, currencyCode = "GBP")
    )

    private fun lineItem(image: CartFragment.Image?) = CartFragment.LineItem(
        id = "line-1",
        productId = "p-1",
        variantId = "v-1",
        sku = "sku-1",
        slug = "oxford-shirt",
        name = "Oxford Shirt",
        quantity = 2,
        image = image,
        price = CartFragment.Price(__typename = "Money", moneyFragment = gbp(45.0)),
        lineTotal = CartFragment.LineTotal(__typename = "Money", moneyFragment = gbp(90.0))
    )

    private fun cart(vararg lines: CartFragment.LineItem, subtotal: Double) = CartFragment(
        id = "cart-1",
        lineItems = lines.toList(),
        totals = CartFragment.Totals(
            subtotal = CartFragment.Subtotal(__typename = "Money", moneyFragment = gbp(subtotal))
        )
    )
}
