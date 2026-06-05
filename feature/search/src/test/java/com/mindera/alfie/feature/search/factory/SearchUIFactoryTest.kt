package com.mindera.alfie.feature.search.factory

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.feature.search.searchSuggestions
import com.mindera.alfie.feature.search.searchUI
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.search.model.SearchSuggestion
import com.mindera.alfie.repository.search.model.SearchSuggestions
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money
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

@ExtendWith(MockKExtension::class)
class SearchUIFactoryTest {

    @RelaxedMockK
    private lateinit var dispatcherProvider: DispatcherProvider

    @InjectMockKs
    private lateinit var uiFactory: SearchUIFactory

    @BeforeEach
    fun setUp() {
        every { dispatcherProvider.default() } returns Dispatchers.Default
    }

    @Test
    fun `invoke - correctly maps to UI model`() = runTest {
        val result = uiFactory(
            searchTerm = "query",
            searchSuggestions = searchSuggestions,
            onProductClick = { }
        )

        assertEquals(searchUI.searchTerm, result.searchTerm)
        assertEquals(searchUI.keywords, result.keywords)
        assertEquals(searchUI.brands, result.brands)
        assertEquals(searchUI.products.size, result.products.size)
        searchUI.products.forEachIndexed { index, expected ->
            val actual = result.products[index]
            assertEquals(expected.id, actual.id)
            assertEquals(expected.slug, actual.slug)
            assertEquals(expected.productCardData.brand, actual.productCardData.brand)
            assertEquals(expected.productCardData.name, actual.productCardData.name)
            assertEquals(expected.productCardData.price, actual.productCardData.price)
            assertEquals(expected.productCardData.image, actual.productCardData.image)
        }
    }

    @Test
    fun `invoke - WHEN onProductClick provided THEN each product card onClick is wired to call it with product id`() = runTest {
        val clickedIds = mutableListOf<String>()

        val result = uiFactory(
            searchTerm = "query",
            searchSuggestions = searchSuggestions,
            onProductClick = { id -> clickedIds.add(id) }
        )

        result.products.forEachIndexed { index, productSuggestionUI ->
            val onClick = productSuggestionUI.productCardData.onClick
            assertNotNull(onClick, "Expected onClick to be set for product at index $index")
            onClick()
        }

        val expectedSlugs = searchSuggestions.products.map { it.slug }
        assertEquals(expectedSlugs, clickedIds)
    }

    @Test
    fun `invoke - WHEN has more than 6 keywords THEN maps only the first 6 keywords`() = runTest {
        val keywords = List(size = 10) {
            SearchSuggestion.Keyword(
                value = "keyword #$it",
                resultCount = 1
            )
        }
        val searchSuggestions = SearchSuggestions(
            keywords = keywords,
            brands = emptyList(),
            products = emptyList()
        )

        val result = uiFactory(
            searchTerm = "query",
            searchSuggestions = searchSuggestions,
            onProductClick = { }
        )

        assertEquals(6, result.keywords.size)
    }

    @Test
    fun `invoke - WHEN has more than 6 brands THEN maps only the first 6 brands`() = runTest {
        val brands = List(size = 10) {
            SearchSuggestion.Brand(
                name = "brand #$it",
                slug = "brand",
                resultCount = 1
            )
        }
        val searchSuggestions = SearchSuggestions(
            keywords = emptyList(),
            brands = brands,
            products = emptyList()
        )

        val result = uiFactory(
            searchTerm = "query",
            searchSuggestions = searchSuggestions,
            onProductClick = { }
        )

        assertEquals(6, result.brands.size)
    }

    @Test
    fun `invoke - WHEN has more than 8 products THEN maps only the first 8 products`() = runTest {
        val products = List(size = 10) {
            SearchSuggestion.Product(
                id = "$it",
                name = "Product",
                brandName = "Brand",
                slug = "product",
                price = Price(
                    amount = Money(
                        amount = 100,
                        amountFormatted = "$100",
                        currencyCode = "AUS"
                    ),
                    was = null
                ),
                media = listOf(
                    Media.Image(
                        url = "",
                        alt = "Media"
                    )
                )
            )
        }
        val searchSuggestions = SearchSuggestions(
            keywords = emptyList(),
            brands = emptyList(),
            products = products
        )

        val result = uiFactory(
            searchTerm = "query",
            searchSuggestions = searchSuggestions,
            onProductClick = { }
        )

        assertEquals(8, result.products.size)
    }
}
