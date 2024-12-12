package au.com.alfie.ecomm.feature.shop

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ShopDeeplinksTest {

    private val shopDeeplinks = ShopDeeplinks()

    @Test
    fun testSuccessfullyMatchesShopDeeplink() {
        val url = "https://www.alfie.com/shop"

        val result = shopDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesShopDeeplinkWithTrailingSlash() {
        val url = "https://www.alfie.com/shop/"

        val result = shopDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesBrandDeeplink() {
        val url = "https://www.alfie.com/brand"

        val result = shopDeeplinks.interpreters[1].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesBrandDeeplinkWithTrailingSlash() {
        val url = "https://www.alfie.com/brand/"

        val result = shopDeeplinks.interpreters[1].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesBrandsDeeplink() {
        val url = "https://www.alfie.com/brands"

        val result = shopDeeplinks.interpreters[1].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesServiceDeeplink() {
        val url = "https://www.alfie.com/services"

        val result = shopDeeplinks.interpreters[2].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesServicesStoreServicesDeeplink() {
        val url = "https://www.alfie.com/services/store-services"

        val result = shopDeeplinks.interpreters[2].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesStoreServicesDeeplink() {
        val url = "https://www.alfie.com/store-services"

        val result = shopDeeplinks.interpreters[2].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }
}
