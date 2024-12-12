package au.com.alfie.ecomm.repository.search.model

import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.shared.model.Money
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SearchSuggestionsTest {

    @Test
    fun `isEmpty - WHEN all fields are empty THEN returns true`() {
        val searchSuggestions = SearchSuggestions(
            keywords = emptyList(),
            brands = emptyList(),
            products = emptyList()
        )

        val result = searchSuggestions.isEmpty()

        assertTrue(result)
    }

    @Test
    fun `isEmpty - WHEN all fields are not empty THEN returns false`() {
        val searchSuggestions = SearchSuggestions(
            keywords = listOf(SearchSuggestion.Keyword(value = "Keyword", resultCount = 1)),
            brands = listOf(SearchSuggestion.Brand(name = "Brand", slug = "brand", resultCount = 1)),
            products = listOf(
                SearchSuggestion.Product(
                    id = "12345",
                    name = "Product",
                    slug = "product",
                    brandName = "Brand",
                    price = Price(
                        amount = Money(
                            currencyCode = "AUD",
                            amount = 40000,
                            amountFormatted = "$400.00"
                        ),
                        was = null
                    ),
                    media = emptyList()
                )
            )
        )

        val result = searchSuggestions.isEmpty()

        assertFalse(result)
    }

    @Test
    fun `isEmpty - WHEN only keywords is empty THEN returns false`() {
        val searchSuggestions = SearchSuggestions(
            keywords = emptyList(),
            brands = listOf(SearchSuggestion.Brand(name = "Brand", slug = "brand", resultCount = 1)),
            products = listOf(
                SearchSuggestion.Product(
                    id = "12345",
                    name = "Product",
                    slug = "product",
                    brandName = "Brand",
                    price = Price(
                        amount = Money(
                            currencyCode = "AUD",
                            amount = 40000,
                            amountFormatted = "$400.00"
                        ),
                        was = null
                    ),
                    media = emptyList()
                )
            )
        )

        val result = searchSuggestions.isEmpty()

        assertFalse(result)
    }

    @Test
    fun `isEmpty - WHEN only brands is empty THEN returns false`() {
        val searchSuggestions = SearchSuggestions(
            keywords = listOf(SearchSuggestion.Keyword(value = "Keyword", resultCount = 1)),
            brands = emptyList(),
            products = listOf(
                SearchSuggestion.Product(
                    id = "12345",
                    name = "Product",
                    slug = "product",
                    brandName = "Brand",
                    price = Price(
                        amount = Money(
                            currencyCode = "AUD",
                            amount = 40000,
                            amountFormatted = "$400.00"
                        ),
                        was = null
                    ),
                    media = emptyList()
                )
            )
        )

        val result = searchSuggestions.isEmpty()

        assertFalse(result)
    }

    @Test
    fun `isEmpty - WHEN only products is empty THEN returns false`() {
        val searchSuggestions = SearchSuggestions(
            keywords = listOf(SearchSuggestion.Keyword(value = "Keyword", resultCount = 1)),
            brands = listOf(SearchSuggestion.Brand(name = "Brand", slug = "brand", resultCount = 1)),
            products = emptyList()
        )

        val result = searchSuggestions.isEmpty()

        assertFalse(result)
    }
}
