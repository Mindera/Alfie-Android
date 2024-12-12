package au.com.alfie.ecomm.feature.account

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class AccountDeeplinksTest {

    private val accountDeeplinks = AccountDeeplinks()

    @Test
    fun testSuccessfullyMatchesAccountDeeplink() {
        val url = "https://www.alfie.com/account"

        val result = accountDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesAccountDeeplinkWithTrailingSlash() {
        val url = "https://www.alfie.com/account/"

        val result = accountDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }
}
