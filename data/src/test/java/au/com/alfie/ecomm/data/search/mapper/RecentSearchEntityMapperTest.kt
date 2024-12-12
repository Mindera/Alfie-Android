package au.com.alfie.ecomm.data.search.mapper

import au.com.alfie.ecomm.data.database.search.model.RecentSearchEntity
import au.com.alfie.ecomm.repository.search.model.RecentSearch
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@ExtendWith(MockKExtension::class)
internal class RecentSearchEntityMapperTest {

    @Test
    fun `test - Query RecentSearch toEntity`() = runTest {
        val recentSearch = RecentSearch.Query(
            searchTerm = "mapper to entity test"
        )

        val result = recentSearch.toEntity()

        assertIs<RecentSearchEntity>(result)
        assertEquals(RecentSearchEntity.Type.QUERY, result.type)
        assertEquals("mapper to entity test", result.searchTerm)
        assertNull(result.slug)
    }

    @Test
    fun `test - Brand RecentSearch toEntity`() = runTest {
        val recentSearch = RecentSearch.Brand(
            searchTerm = "mapper to entity test",
            slug = "brand-slug"
        )

        val result = recentSearch.toEntity()

        assertIs<RecentSearchEntity>(result)
        assertEquals(RecentSearchEntity.Type.BRAND, result.type)
        assertEquals("mapper to entity test", result.searchTerm)
        assertEquals("brand-slug", result.slug)
    }

    @Test
    fun `test - List of RecentSearchEntity toDomain`() = runTest {
        val recentSearchEntityList = listOf(
            RecentSearchEntity(
                searchTerm = "search #1",
                searchTimestamp = 12345L,
                type = RecentSearchEntity.Type.QUERY
            ),
            RecentSearchEntity(
                searchTerm = "search #2",
                searchTimestamp = 123456L,
                type = RecentSearchEntity.Type.BRAND,
                slug = "brand-slug"
            )
        )

        val expected = listOf(
            RecentSearch.Query("search #1"),
            RecentSearch.Brand(
                searchTerm = "search #2",
                slug = "brand-slug"
            )
        )

        val result = recentSearchEntityList.toDomain()

        assertEquals(expected, result)
    }
}
