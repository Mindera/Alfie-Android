package com.mindera.alfie.data.navigation.remote.mapper

import com.mindera.alfie.data.navigation.leafMenuItem
import com.mindera.alfie.data.navigation.mainMenuData
import com.mindera.alfie.data.navigation.menuItem
import com.mindera.alfie.data.navigation.navEntriesData
import com.mindera.alfie.data.navigation.navEntryEntitiesFromGraph
import com.mindera.alfie.data.navigation.subMenuItem
import com.mindera.alfie.repository.navigation.model.NavItemType
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pins the `mainMenu` → entity contract. Ported from the iOS suite (Alfie-iOS PR #99,
 * `MainMenuConverterTests`) so both platforms resolve collection handles identically, plus a case
 * for the host-only-url defect that shipped there.
 */
@ExtendWith(MockKExtension::class)
internal class RemoteNavigationMapperTest {

    @Test
    fun `GIVEN a two level menu WHEN mapped THEN the whole tree is preserved`() = runTest {
        assertEquals(navEntryEntitiesFromGraph, navEntriesData.toEntity())
    }

    @Test
    fun `GIVEN an empty menu WHEN mapped THEN no entries are produced`() = runTest {
        assertEquals(emptyList(), mainMenuData().toEntity())
    }

    @Test
    fun `GIVEN a leaf WHEN mapped THEN it is a childless listing with its handle`() = runTest {
        val result = mainMenuData(menuItem(title = "Women", url = "/women")).toEntity()

        assertEquals(1, result.size)
        assertEquals("Women", result.first().title)
        assertEquals("women", result.first().path)
        assertEquals(NavItemType.LISTING.name, result.first().navItemType)
        assertEquals(false, result.first().hasChildren)
        assertEquals(emptyList(), result.first().items)
    }

    @Test
    fun `GIVEN a multi segment url WHEN mapped THEN it reduces to the last path component`() = runTest {
        val result = mainMenuData(menuItem(title = "Dresses", url = "/shop/new/dresses")).toEntity()

        assertEquals("dresses", result.first().path)
    }

    @Test
    fun `GIVEN three levels of nesting WHEN mapped THEN all are preserved and the deepest is a leaf`() = runTest {
        val result = mainMenuData(
            menuItem(
                title = "Women",
                url = "/women",
                items = listOf(
                    subMenuItem(
                        title = "Clothes",
                        url = "/clothes",
                        items = listOf(leafMenuItem(title = "Dresses", url = "/dresses"))
                    )
                )
            )
        ).toEntity()

        val level2 = result.first().items.first()
        val level3 = level2.items.first()

        assertTrue(result.first().hasChildren)
        assertTrue(level2.hasChildren)
        assertEquals("dresses", level3.path)
        assertEquals(false, level3.hasChildren)
        assertEquals(emptyList(), level3.items)
    }

    @Test
    fun `GIVEN a leaf without a url WHEN mapped THEN it is dropped`() = runTest {
        assertEquals(emptyList(), mainMenuData(menuItem(title = "Women", url = null)).toEntity())
    }

    @Test
    fun `GIVEN a parent without a url WHEN it has children THEN it survives`() = runTest {
        val result = mainMenuData(
            menuItem(
                title = "Women",
                url = null,
                items = listOf(subMenuItem(title = "Dresses", url = "/dresses"))
            )
        ).toEntity()

        assertEquals(1, result.size)
        assertEquals("", result.first().path)
        assertTrue(result.first().hasChildren)
    }

    @Test
    fun `GIVEN any menu entry WHEN mapped THEN its type is always LISTING`() = runTest {
        val result = mainMenuData(
            menuItem(title = "Page", url = "/pages/about"),
            menuItem(title = "External", url = "https://example.com/collections/sale")
        ).toEntity()

        assertTrue(result.all { it.navItemType == NavItemType.LISTING.name })
    }

    @Test
    fun `GIVEN a root url WHEN mapped THEN it is dropped`() = runTest {
        assertEquals(emptyList(), mainMenuData(menuItem(title = "Root", url = "/")).toEntity())
    }

    @Test
    fun `GIVEN a query string WHEN mapped THEN it is stripped`() = runTest {
        val result = mainMenuData(menuItem(title = "Sale", url = "/collections/sale?filter=color")).toEntity()

        assertEquals("sale", result.first().path)
    }

    @Test
    fun `GIVEN a fragment WHEN mapped THEN it is stripped`() = runTest {
        val result = mainMenuData(menuItem(title = "Dresses", url = "/dresses#top")).toEntity()

        assertEquals("dresses", result.first().path)
    }

    @Test
    fun `GIVEN an empty url WHEN mapped THEN it is dropped`() = runTest {
        assertEquals(emptyList(), mainMenuData(menuItem(title = "Women", url = "")).toEntity())
    }

    @Test
    fun `GIVEN a whitespace only url WHEN mapped THEN it is dropped`() = runTest {
        assertEquals(emptyList(), mainMenuData(menuItem(title = "Women", url = "   ")).toEntity())
    }

    @Test
    fun `GIVEN a trailing slash WHEN mapped THEN it reduces to the last segment`() = runTest {
        val result = mainMenuData(menuItem(title = "Dresses", url = "/dresses/")).toEntity()

        assertEquals("dresses", result.first().path)
    }

    @Test
    fun `GIVEN an absolute url with a path WHEN mapped THEN it reduces to the last path segment`() = runTest {
        val result = mainMenuData(
            menuItem(title = "Dresses", url = "https://shop.example.com/collections/dresses")
        ).toEntity()

        assertEquals("dresses", result.first().path)
    }

    @Test
    fun `GIVEN a host only absolute url WHEN mapped THEN it is dropped`() = runTest {
        // Regression: the iOS converter yields "/example.com" here, because it splits the raw
        // string instead of reading the parsed path.
        assertEquals(emptyList(), mainMenuData(menuItem(title = "Home", url = "https://example.com")).toEntity())
    }

    @Test
    fun `GIVEN a mixed case handle WHEN mapped THEN it is lowercased`() = runTest {
        val result = mainMenuData(menuItem(title = "Tops", url = "/Womens-Tops")).toEntity()

        assertEquals("womens-tops", result.first().path)
    }

    @Test
    fun `GIVEN a special category url WHEN mapped THEN it stays matchable as a bare handle`() = runTest {
        val result = mainMenuData(
            menuItem(title = "Brands", url = "/brands"),
            menuItem(title = "Services", url = "/store-services")
        ).toEntity()

        // Menu urls arrive leading-slashed; navigation matches on the bare handle, so the slash has
        // to be reduced away here rather than at every call site.
        assertEquals(listOf("brands", "store-services"), result.map { it.path })
    }

    @Test
    fun `GIVEN a parent whose children are all dropped WHEN mapped THEN it degrades to a leaf`() = runTest {
        val result = mainMenuData(
            menuItem(
                title = "Women",
                url = "/women",
                items = listOf(subMenuItem(title = "Broken", url = null))
            )
        ).toEntity()

        assertEquals(1, result.size)
        assertEquals("women", result.first().path)
        assertEquals(false, result.first().hasChildren)
        assertEquals(emptyList(), result.first().items)
    }

    @Test
    fun `GIVEN a null items list WHEN mapped THEN the entry has no children`() = runTest {
        val result = mainMenuData(menuItem(title = "Women", url = "/women", items = null)).toEntity()

        assertEquals(false, result.first().hasChildren)
    }
}
