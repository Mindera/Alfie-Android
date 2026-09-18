package com.mindera.alfie.feature.pdp.factory

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.productlist.model.ProductListPriceRange
import com.mindera.alfie.repository.shared.model.Media
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
internal class RelatedProductsUIFactoryTest {

    private companion object {
        const val WISHLISTED_SLUG = "wishlisted-product"
        const val PLAIN_SLUG = "plain-product"

        fun entry(
            slug: String,
            brandName: String? = "Brand",
            tags: List<String> = listOf("Best Seller")
        ) = ProductListEntry(
            id = "id-$slug",
            slug = slug,
            name = "Product $slug",
            brandName = brandName,
            productType = "Clothing",
            primaryImage = Media.Image(url = "https://alfie.test/$slug.jpg", alt = "alt"),
            priceRange = ProductListPriceRange(minAmount = 100.0, maxAmount = 100.0, currencyCode = "GBP"),
            inventoryTotal = 5,
            tags = tags
        )
    }

    @RelaxedMockK
    private lateinit var dispatcherProvider: DispatcherProvider

    @InjectMockKs
    private lateinit var uiFactory: RelatedProductsUIFactory

    @BeforeEach
    fun setUp() {
        every { dispatcherProvider.default() } returns Dispatchers.Default
    }

    @Test
    fun `invoke - maps an entry onto a vertical product card`() = runTest {
        val entry = entry(PLAIN_SLUG)

        val result = uiFactory(
            entries = listOf(entry),
            wishlistedSlugs = emptyList(),
            onProductClick = { },
            onFavoriteClick = { }
        )

        assertEquals(1, result.size)
        val item = result.first()
        assertEquals(PLAIN_SLUG, item.slug)
        assertEquals("Brand", item.productCardData.brand)
        assertEquals("Product $PLAIN_SLUG", item.productCardData.name)
        assertEquals("Best Seller", item.productCardData.label)
    }

    @Test
    fun `invoke - WHEN brand and tags are absent THEN brand is blank and no label is set`() = runTest {
        val result = uiFactory(
            entries = listOf(entry(PLAIN_SLUG, brandName = null, tags = emptyList())),
            wishlistedSlugs = emptyList(),
            onProductClick = { },
            onFavoriteClick = { }
        )

        assertEquals("", result.first().productCardData.brand)
        assertNull(result.first().productCardData.label)
    }

    @Test
    fun `invoke - marks only the entries whose slug is wishlisted`() = runTest {
        val result = uiFactory(
            entries = listOf(entry(WISHLISTED_SLUG), entry(PLAIN_SLUG)),
            wishlistedSlugs = listOf(WISHLISTED_SLUG),
            onProductClick = { },
            onFavoriteClick = { }
        )

        assertTrue(result.first { it.slug == WISHLISTED_SLUG }.isWishlisted)
        assertFalse(result.first { it.slug == PLAIN_SLUG }.isWishlisted)
    }

    @Test
    fun `invoke - wires the card onClick to the entry's own slug`() = runTest {
        var clicked: String? = null

        val result = uiFactory(
            entries = listOf(entry(WISHLISTED_SLUG), entry(PLAIN_SLUG)),
            wishlistedSlugs = emptyList(),
            onProductClick = { clicked = it },
            onFavoriteClick = { }
        )

        val onClick = result.first { it.slug == PLAIN_SLUG }.productCardData.onClick
        assertNotNull(onClick)
        onClick()

        assertEquals(PLAIN_SLUG, clicked)
    }

    @Test
    fun `invoke - wires the card onFavoriteClick to the entry's own slug`() = runTest {
        var favorited: String? = null

        val result = uiFactory(
            entries = listOf(entry(WISHLISTED_SLUG), entry(PLAIN_SLUG)),
            wishlistedSlugs = emptyList(),
            onProductClick = { },
            onFavoriteClick = { favorited = it }
        )

        val onFavoriteClick = result.first { it.slug == PLAIN_SLUG }.productCardData.onFavoriteClick
        assertNotNull(onFavoriteClick)
        onFavoriteClick()

        assertEquals(PLAIN_SLUG, favorited)
    }

    @Test
    fun `invoke - WHEN there are no entries THEN the result is empty`() = runTest {
        val result = uiFactory(
            entries = emptyList(),
            wishlistedSlugs = listOf(WISHLISTED_SLUG),
            onProductClick = { },
            onFavoriteClick = { }
        )

        assertTrue(result.isEmpty())
    }
}
