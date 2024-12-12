package au.com.alfie.ecomm.feature.home

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class HomeDeeplinksTest {

    private val homeDeeplinks = HomeDeeplinks()

    @Test
    fun testSuccessfullyMatchesHomepageDeeplink() {
        val url = "https://www.alfie.com"

        val result = homeDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesHomepageDeeplinkWithTrailingSlash() {
        val url = "https://www.alfie.com/"

        val result = homeDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }
}
