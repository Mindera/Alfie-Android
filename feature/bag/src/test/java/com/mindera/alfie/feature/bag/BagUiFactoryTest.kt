package com.mindera.alfie.feature.bag

import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.feature.bag.models.BagContentUi
import com.mindera.alfie.feature.bag.models.BagItemNotice
import com.mindera.alfie.feature.mappers.toImageUI
import com.mindera.alfie.repository.bag.Bag
import com.mindera.alfie.repository.shared.model.Media
import kotlinx.collections.immutable.toImmutableList
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BagUiFactoryTest {

    private val uiFactory = BagUiFactory()

    @Test
    fun `invoke - maps the Cart and its products to the Bag UI`() {
        val result = uiFactory(bag = bag, productsBySlug = productsBySlug, onProductClick = { })

        assertEquals(bagContentUi, result.withoutClicks())
    }

    @Test
    fun `invoke - WHEN a line is tapped THEN it opens the PDP by the line's slug`() {
        val clicked = mutableListOf<String>()

        val content = uiFactory(bag = bag, productsBySlug = productsBySlug, onProductClick = { clicked += it })
        content.lines.forEach { it.productCardData.onClick?.invoke() }

        assertEquals(listOf("123456-product", "654321-product"), clicked)
    }

    @Test
    fun `invoke - quantity, price and total come from the Cart`() {
        val line = bagLines[1].copy(quantity = 3, unitPrice = aud(90.0), lineTotal = aud(270.0))

        val content = uiFactory(
            bag = Bag(lines = listOf(line), subtotal = aud(270.0)),
            productsBySlug = productsBySlug,
            onProductClick = { }
        )

        assertEquals(3, content.lines.single().productCardData.quantity)
        assertEquals(PriceType.Default(price = "$90"), content.lines.single().productCardData.price)
        assertEquals("$270", content.summary.totalFormatted)
    }

    @Test
    fun `invoke - WHEN the product's was-price is not above the Cart price THEN no sale is shown`() {
        val line = bagLines[0].copy(unitPrice = aud(250.0))

        val content = uiFactory(bag = Bag(listOf(line), aud(250.0)), productsBySlug = productsBySlug, onProductClick = { })

        assertEquals(PriceType.Default(price = "$250"), content.lines.single().productCardData.price)
    }

    @Test
    fun `invoke - WHEN a product didn't load THEN its line falls back to the Cart's name, image and price`() {
        val image = Media.Image(url = "https://cart/image.jpg", alt = "cart image")
        val line = bagLines[0].copy(image = image)

        val content = uiFactory(bag = Bag(listOf(line), aud(100.0)), productsBySlug = emptyMap(), onProductClick = { })

        val shown = content.lines.single()
        assertEquals("Cart name 1", shown.productCardData.name)
        assertEquals(image.toImageUI(), shown.productCardData.image)
        assertEquals("", shown.productCardData.brand)
        assertEquals(PriceType.Default(price = "$100"), shown.productCardData.price)
        assertTrue(shown.productCardData.isAvailable)
        assertNull(shown.notice)
    }

    @Test
    fun `invoke - WHEN the line has no variant id THEN its SKU still resolves the variant`() {
        val line = bagLines[1].copy(variantId = null)

        val content = uiFactory(bag = Bag(listOf(line), aud(100.0)), productsBySlug = productsBySlug, onProductClick = { })

        assertEquals("M", content.lines.single().productCardData.size)
    }

    @Test
    fun `invoke - WHEN the variant is gone from the product THEN the line is unavailable rather than another variant`() {
        val line = bagLines[1].copy(variantId = "gone", sku = "gone")

        val content = uiFactory(bag = Bag(listOf(line), aud(100.0)), productsBySlug = productsBySlug, onProductClick = { })

        val shown = content.lines.single()
        assertFalse(shown.productCardData.isAvailable)
        assertEquals(BagItemNotice.Unavailable, shown.notice)
        assertEquals("", shown.productCardData.color)
    }

    @Test
    fun `invoke - WHEN the Bag holds more units than are in stock THEN the notice says so`() {
        val line = bagLines[0].copy(quantity = 2)

        val content = uiFactory(bag = Bag(listOf(line), aud(200.0)), productsBySlug = productsBySlug, onProductClick = { })

        assertEquals(BagItemNotice.ExceedsStock(remaining = 1), content.lines.single().notice)
    }

    @Test
    fun `invoke - WHEN the Bag is empty THEN there are no lines and no total`() {
        val content = uiFactory(bag = Bag.EMPTY, productsBySlug = productsBySlug, onProductClick = { })

        assertTrue(content.lines.isEmpty())
        assertNull(content.summary.totalFormatted)
    }

    // onClick is a lambda and never equal across instances, so compare with it stripped.
    private fun BagContentUi.withoutClicks() = copy(
        lines = lines.map { it.copy(productCardData = it.productCardData.copy(onClick = null)) }.toImmutableList()
    )
}
