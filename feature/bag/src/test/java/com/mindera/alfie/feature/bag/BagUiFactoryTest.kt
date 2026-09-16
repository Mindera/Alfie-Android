package com.mindera.alfie.feature.bag

import com.mindera.alfie.feature.bag.models.BagItemNotice
import com.mindera.alfie.repository.bag.BagProduct
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class BagUiFactoryTest {

    @InjectMockKs
    private lateinit var uiFactory: BagUiFactory

    @Test
    fun `invoke - map to expected Bag UI`() = runTest {
        val result = uiFactory(
            bagProducts = bagProducts,
            products = products,
            onProductClick = { }
        )

        // onClick is a lambda and never equal across instances, so compare with it stripped.
        val stripped = result.copy(
            items = result.items
                .map { item -> item.copy(productCardData = item.productCardData.copy(onClick = null)) }
                .toImmutableList()
        )
        assertEquals(bagContentUi, stripped)
    }

    @Test
    fun `invoke - WHEN onProductClick provided THEN each product card onClick is wired to call it with product slug`() = runTest {
        val clickedIds = mutableListOf<String>()

        val content = uiFactory(
            bagProducts = bagProducts,
            products = products,
            onProductClick = { id -> clickedIds.add(id) }
        )

        content.items.forEachIndexed { index, bagProductUi ->
            val onClick = bagProductUi.productCardData.onClick
            assertNotNull(onClick, "Expected onClick to be set for item at index $index")
            onClick()
        }

        val expectedSlugs = products.map { it.slug }
        assertEquals(expectedSlugs, clickedIds)
    }

    @Test
    fun `invoke - WHEN the same variant is added twice THEN it collapses into one line with quantity 2`() = runTest {
        val duplicated = listOf(bagProducts[0], bagProducts[0], bagProducts[1])

        val content = uiFactory(
            bagProducts = duplicated,
            products = products,
            onProductClick = { }
        )

        assertEquals(2, content.items.size)
        assertEquals(2, content.items[0].productCardData.quantity)
        assertEquals(1, content.items[1].productCardData.quantity)
    }

    @Test
    fun `invoke - WHEN a line has quantity above one THEN the total multiplies the unit price`() = runTest {
        val duplicated = listOf(bagProducts[0], bagProducts[0], bagProducts[1])

        val content = uiFactory(
            bagProducts = duplicated,
            products = products,
            onProductClick = { }
        )

        // 100.00 x 2 + 100.00 x 1
        assertEquals("$300.00", content.summary.totalFormatted)
    }

    @Test
    fun `invoke - resolves the variant matching the bag entry sku`() = runTest {
        val content = uiFactory(
            bagProducts = bagProducts,
            products = products,
            onProductClick = { }
        )

        assertEquals("variant1", content.items[0].productCardData.reference)
        assertEquals("variant11", content.items[1].productCardData.reference)
    }

    @Test
    fun `invoke - WHEN stock is at or below the low stock threshold THEN the line carries a low stock notice`() = runTest {
        val content = uiFactory(
            bagProducts = bagProducts,
            products = products,
            onProductClick = { }
        )

        assertEquals(BagItemNotice.LowStock(remaining = 1), content.items[0].notice)
        assertTrue(content.items[0].productCardData.isAvailable)
        assertNull(content.items[1].notice)
    }

    @Test
    fun `invoke - WHEN a variant is out of stock THEN the line is unavailable and carries the notice`() = runTest {
        val soldOut = products.map { product ->
            product.copy(variants = product.variants.map { it.copy(availableQuantity = 0) })
        }

        val content = uiFactory(
            bagProducts = bagProducts,
            products = soldOut,
            onProductClick = { }
        )

        assertEquals(BagItemNotice.Unavailable, content.items[0].notice)
        assertFalse(content.items[0].productCardData.isAvailable)
    }

    @Test
    fun `invoke - WHEN a slug and sku split differently THEN the line keys stay distinct`() = runTest {
        // Joined plainly, ("a-b", "c") and ("a", "b-c") both flatten to "a-b-c" — a duplicate key.
        val entries = listOf(
            BagProduct(productId = "a-b", variantSku = "c"),
            BagProduct(productId = "a", variantSku = "b-c")
        )
        val catalogue = listOf(
            products[0].copy(slug = "a-b", variants = products[0].variants.map { it.copy(sku = "c") }),
            products[0].copy(slug = "a", variants = products[0].variants.map { it.copy(sku = "b-c") })
        )

        val content = uiFactory(
            bagProducts = entries,
            products = catalogue,
            onProductClick = { }
        )

        assertEquals(2, content.items.size)
        assertEquals(2, content.items.map { it.id }.toSet().size)
    }

    @Test
    fun `invoke - WHEN the saved sku is gone THEN the line is unavailable rather than another variant`() = runTest {
        // The product still loads, but no longer offers the variant the bag holds.
        val renamedSku = products.map { product ->
            product.copy(variants = product.variants.map { it.copy(sku = "some-other-sku") })
        }

        val content = uiFactory(
            bagProducts = bagProducts,
            products = renamedSku,
            onProductClick = { }
        )

        val line = content.items[0].productCardData
        assertFalse(line.isAvailable)
        assertEquals(BagItemNotice.Unavailable, content.items[0].notice)
        // Nothing from the substituted variant leaks into the line, and it adds nothing to the total.
        assertEquals("", line.reference)
        assertEquals("", line.color)
        // The currency still resolves off the product, so the summary reads "$0.00" rather than
        // drawing the "Total" label against an empty string.
        assertEquals("${'$'}0.00", content.summary.totalFormatted)
    }

    @Test
    fun `invoke - WHEN the bag holds more units than are in stock THEN the notice says so`() = runTest {
        // Two units of a variant with one left: "Only 1 item left!" under "Quantity: 2" would read
        // as if nothing were wrong.
        val twoOfTheSame = listOf(bagProducts[0], bagProducts[0])

        val content = uiFactory(
            bagProducts = twoOfTheSame,
            products = products,
            onProductClick = { }
        )

        assertEquals(2, content.items[0].productCardData.quantity)
        assertEquals(BagItemNotice.ExceedsStock(remaining = 1), content.items[0].notice)
        // Still available and still charged in full — the total has to agree with the row's own
        // arithmetic, and the notice carries the problem.
        assertTrue(content.items[0].productCardData.isAvailable)
        assertEquals("${'$'}200.00", content.summary.totalFormatted)
    }

    @Test
    fun `invoke - WHEN a product has no variants THEN the line is unavailable and says so`() = runTest {
        val variantless = products.map { it.copy(variants = emptyList()) }

        val content = uiFactory(
            bagProducts = bagProducts,
            products = variantless,
            onProductClick = { }
        )

        assertFalse(content.items[0].productCardData.isAvailable)
        assertEquals(BagItemNotice.Unavailable, content.items[0].notice)
    }

    @Test
    fun `invoke - WHEN a product failed to load THEN its line is skipped instead of throwing`() = runTest {
        val withUnknown = bagProducts + BagProduct(productId = "missing-product", variantSku = "missing")

        val content = uiFactory(
            bagProducts = withUnknown,
            products = products,
            onProductClick = { }
        )

        assertEquals(2, content.items.size)
    }

    @Test
    fun `invoke - WHEN the bag is empty THEN there are no items and no total`() = runTest {
        val content = uiFactory(
            bagProducts = emptyList(),
            products = products,
            onProductClick = { }
        )

        assertTrue(content.items.isEmpty())
        assertEquals("", content.summary.totalFormatted)
    }
}
