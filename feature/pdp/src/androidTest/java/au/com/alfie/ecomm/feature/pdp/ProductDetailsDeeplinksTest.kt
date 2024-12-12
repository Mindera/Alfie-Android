package au.com.alfie.ecomm.feature.pdp

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ProductDetailsDeeplinksTest {

    private val productDetailsDeeplinks = ProductDetailsDeeplinks()

    @Test
    fun testSuccessfullyMatchesBrandPLPDeeplink() {
        val url = "https://www.alfie.com/product/346123285"

        val result = productDetailsDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testArgumentWithSlugSuccessfullyMatchesBrandPLPDeeplink() {
        val url = "https://www.alfie.com/product/papinelle-coco-organic-cotton-pj-346123285"

        val result = productDetailsDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }
}
