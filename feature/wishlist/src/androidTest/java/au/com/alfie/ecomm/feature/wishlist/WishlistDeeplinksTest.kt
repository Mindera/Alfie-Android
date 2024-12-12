package au.com.alfie.ecomm.feature.wishlist

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class WishlistDeeplinksTest {

    private val wishlistDeeplinks = WishlistDeeplinks()

    @Test
    fun testSuccessfullyMatchesWishlistDeeplink() {
        val url = "https://www.alfie.com/wishlist"

        val result = wishlistDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesWishlistDeeplinkWithTrailingSlash() {
        val url = "https://www.alfie.com/wishlist/"

        val result = wishlistDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }
}
