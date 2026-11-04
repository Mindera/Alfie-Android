package com.mindera.alfie.data.search.mapper

import com.mindera.alfie.graphql.bff.SearchSuggestionsQuery
import com.mindera.alfie.graphql.bff.fragment.ImageFragment
import com.mindera.alfie.graphql.bff.fragment.MoneyFragment
import com.mindera.alfie.repository.search.model.SearchSuggestion
import com.mindera.alfie.repository.search.model.SearchSuggestions
import com.mindera.alfie.repository.shared.model.Media
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SearchSuggestionMapperTest {

    @Test
    fun `toDomain - maps every section of the BFF suggestion`() {
        val suggestion = SearchSuggestionsQuery.Suggestion(
            brands = listOf(SearchSuggestionsQuery.Brand(key = "935", value = "Kashmere", results = 12)),
            keywords = listOf(SearchSuggestionsQuery.Keyword(value = "shirt", results = null)),
            products = listOf(
                SearchSuggestionsQuery.Product(
                    id = "gid://shopify/Product/1",
                    name = "T-Shirt",
                    brandName = "Alfie Basics",
                    slug = "t-shirt",
                    price = SearchSuggestionsQuery.Price(
                        __typename = "Money",
                        moneyFragment = MoneyFragment(amount = 8.0, currencyCode = "GBP")
                    ),
                    images = listOf(
                        SearchSuggestionsQuery.Image(
                            __typename = "Image",
                            imageFragment = ImageFragment(url = "https://cdn/t-shirt.jpg", altText = "T-Shirt")
                        )
                    )
                )
            )
        )

        val result = suggestion.toDomain()

        assertEquals(listOf(SearchSuggestion.Brand(key = "935", name = "Kashmere", resultCount = 12)), result.brands)
        assertEquals(listOf(SearchSuggestion.Keyword(value = "shirt", resultCount = null)), result.keywords)
        val product = result.products.single()
        assertEquals("gid://shopify/Product/1", product.id)
        assertEquals("T-Shirt", product.name)
        assertEquals("Alfie Basics", product.brandName)
        assertEquals("t-shirt", product.slug)
        assertEquals(8.0, product.price?.amount)
        assertEquals("GBP", product.price?.currencyCode)
        assertEquals(listOf(Media.Image(url = "https://cdn/t-shirt.jpg", alt = "T-Shirt")), product.images)
    }

    @Test
    fun `toDomain - WHEN the product has no price THEN price is null`() {
        val suggestion = SearchSuggestionsQuery.Suggestion(
            brands = emptyList(),
            keywords = emptyList(),
            products = listOf(
                SearchSuggestionsQuery.Product(
                    id = "1",
                    name = "Unpriced",
                    brandName = null,
                    slug = "unpriced",
                    price = null,
                    images = emptyList()
                )
            )
        )

        val product = suggestion.toDomain().products.single()

        assertNull(product.price)
        assertNull(product.brandName)
    }

    @Test
    fun `toDomain - WHEN every section is empty THEN suggestions are empty`() {
        val suggestion = SearchSuggestionsQuery.Suggestion(
            brands = emptyList(),
            keywords = emptyList(),
            products = emptyList()
        )

        assertEquals(SearchSuggestions(emptyList(), emptyList(), emptyList()), suggestion.toDomain())
        assert(suggestion.toDomain().isEmpty())
    }
}
