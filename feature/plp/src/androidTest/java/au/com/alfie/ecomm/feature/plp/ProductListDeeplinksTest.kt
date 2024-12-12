package au.com.alfie.ecomm.feature.plp

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ProductListDeeplinksTest {

    private val productListDeeplinks = ProductListDeeplinks()

    @Test
    fun testSuccessfullyMatchesBrandPLPDeeplink() {
        val url = "https://www.alfie.com/brand/boss"

        val result = productListDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesBrandPLPDeeplinkWithCategory() {
        val url = "https://www.alfie.com/brand/boss/men"

        val result = productListDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesCategoryPLPDeeplink() {
        val url = "https://www.alfie.com/designer"

        val result = productListDeeplinks.interpreters[1].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesCategoryPLPDeeplinkWith1SubCategory() {
        val url = "https://www.alfie.com/designer/women"

        val result = productListDeeplinks.interpreters[1].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesCategoryPLPDeeplinkWith2SubCategory() {
        val url = "https://www.alfie.com/designer/women/clothing"

        val result = productListDeeplinks.interpreters[1].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesCategoryPLPDeeplinkWith3SubCategory() {
        val url = "https://www.alfie.com/designer/women/clothing/dresses"

        val result = productListDeeplinks.interpreters[1].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesCategoryPLPDeeplinkWith4SubCategory() {
        val url = "https://www.alfie.com/designer/women/clothing/dresses/maxi-dresses"

        val result = productListDeeplinks.interpreters[1].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesSearchPLPDeeplink() {
        val url = "https://www.alfie.com/search?q=something"

        val result = productListDeeplinks.interpreters[2].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }
}
