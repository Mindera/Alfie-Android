package com.mindera.alfie.data.search.mapper

import com.mindera.alfie.data.search.searchSuggestions
import com.mindera.alfie.data.search.searchSuggestionsData
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SearchSuggestionsMapperTest {

    @Test
    fun testSearchSuggestionsMapper() {
        val result = searchSuggestionsData.suggestion.toDomain()

        assertEquals(searchSuggestions, result)
    }
}
