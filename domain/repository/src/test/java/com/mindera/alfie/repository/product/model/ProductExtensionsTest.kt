package com.mindera.alfie.repository.product.model

import com.mindera.alfie.repository.shared.model.Money
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProductExtensionsTest {

    private val stubMoney = Money(amount = 0.0, amountFormatted = "£0", currencyCode = "GBP")

    private fun makeVariant(id: String, available: Boolean) = Variant(
        id = id,
        sku = id,
        price = Price(amount = stubMoney, was = null),
        options = emptyList(),
        media = emptyList(),
        available = available
    )

    private fun makeProduct(
        defaultVariantId: String?,
        variants: List<Variant>
    ) = Product(
        id = "p1",
        name = "Product",
        slug = "product",
        brandName = null,
        descriptionHtml = null,
        defaultVariantId = defaultVariantId,
        images = emptyList(),
        priceRange = null,
        variants = variants
    )

    @Test
    fun `resolveDefaultVariant - returns variant matching defaultVariantId`() {
        val v1 = makeVariant("v1", available = false)
        val v2 = makeVariant("v2", available = true)
        val product = makeProduct(defaultVariantId = "v1", variants = listOf(v1, v2))

        assertEquals(v1, product.resolveDefaultVariant())
    }

    @Test
    fun `resolveDefaultVariant - falls back to first available when defaultVariantId has no match`() {
        val v1 = makeVariant("v1", available = false)
        val v2 = makeVariant("v2", available = true)
        val product = makeProduct(defaultVariantId = "unknown", variants = listOf(v1, v2))

        assertEquals(v2, product.resolveDefaultVariant())
    }

    @Test
    fun `resolveDefaultVariant - falls back to first variant when none available`() {
        val v1 = makeVariant("v1", available = false)
        val v2 = makeVariant("v2", available = false)
        val product = makeProduct(defaultVariantId = null, variants = listOf(v1, v2))

        assertEquals(v1, product.resolveDefaultVariant())
    }

    @Test
    fun `resolveDefaultVariant - returns null when variants list is empty`() {
        val product = makeProduct(defaultVariantId = null, variants = emptyList())

        assertNull(product.resolveDefaultVariant())
    }
}
