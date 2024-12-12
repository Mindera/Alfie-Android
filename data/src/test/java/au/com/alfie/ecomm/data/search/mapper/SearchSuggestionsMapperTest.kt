package au.com.alfie.ecomm.data.search.mapper

import au.com.alfie.ecomm.data.search.searchSuggestions
import au.com.alfie.ecomm.data.search.searchSuggestionsData
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SearchSuggestionsMapperTest {

    @Test
    fun testSearchSuggestionsMapper() {
        val result = searchSuggestionsData.suggestion.toDomain()

        assertEquals(searchSuggestions, result)
    }
}
