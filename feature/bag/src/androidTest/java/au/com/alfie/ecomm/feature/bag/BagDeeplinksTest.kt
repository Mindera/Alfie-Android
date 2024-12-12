package au.com.alfie.ecomm.feature.bag

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class BagDeeplinksTest {

    private val bagDeeplinks = BagDeeplinks()

    @Test
    fun testSuccessfullyMatchesBagDeeplink() {
        val url = "https://www.alfie.com/bag"

        val result = bagDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesBagDeeplinkWithTrailingSlash() {
        val url = "https://www.alfie.com/bag/"

        val result = bagDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }
}
