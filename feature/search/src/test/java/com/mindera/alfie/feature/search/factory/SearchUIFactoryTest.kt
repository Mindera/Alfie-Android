package com.mindera.alfie.feature.search.factory

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.feature.search.emptySearchSuggestions
import com.mindera.alfie.feature.search.model.BrandSuggestionUI
import com.mindera.alfie.feature.search.model.KeywordSuggestionUI
import com.mindera.alfie.feature.search.searchSuggestions
import com.mindera.alfie.repository.search.model.SearchSuggestion
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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
internal class SearchUIFactoryTest {

    @RelaxedMockK
    private lateinit var dispatcherProvider: DispatcherProvider

    @InjectMockKs
    private lateinit var uiFactory: SearchUIFactory

    @BeforeEach
    fun setUp() {
        every { dispatcherProvider.default() } returns Dispatchers.Unconfined
    }

    @Test
    fun `invoke - maps keywords, brands and products`() = runTest {
        val result = uiFactory(
            searchTerm = "sh",
            searchSuggestions = searchSuggestions,
            onProductClick = {}
        )

        assertEquals("sh", result.searchTerm)
        assertEquals(listOf(KeywordSuggestionUI(value = "shirt")), result.keywords)
        assertEquals(listOf(BrandSuggestionUI(key = "935", name = "Kashmere")), result.brands)
        val product = result.products.single()
        assertEquals("12345", product.id)
        assertEquals("t-shirt", product.slug)
        assertEquals("Alfie Basics", product.productCardData.brand)
        assertEquals("T-Shirt", product.productCardData.name)
        assertEquals(PriceType.Default(price = "£8.00"), product.productCardData.price)
        assertEquals(
            "https://alfie.test/t-shirt.jpg",
            (product.productCardData.image.images.single() as ImageSizeUI.Custom).url
        )
    }

    @Test
    fun `invoke - WHEN a product card is clicked THEN reports its slug`() = runTest {
        var clickedSlug: String? = null

        val result = uiFactory(
            searchTerm = "sh",
            searchSuggestions = searchSuggestions,
            onProductClick = { clickedSlug = it }
        )
        assertNotNull(result.products.single().productCardData.onClick).invoke()

        assertEquals("t-shirt", clickedSlug)
    }

    @Test
    fun `invoke - WHEN a product has no price or brand THEN renders them blank`() = runTest {
        val unpriced = SearchSuggestion.Product(
            id = "1",
            name = "Unpriced",
            slug = "unpriced",
            brandName = null,
            price = null,
            images = emptyList()
        )

        val result = uiFactory(
            searchTerm = "un",
            searchSuggestions = emptySearchSuggestions.copy(products = listOf(unpriced)),
            onProductClick = {}
        )

        val card = result.products.single().productCardData
        assertEquals(PriceType.Default(price = ""), card.price)
        assertTrue(card.brand.isEmpty())
    }
}
