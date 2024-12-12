package au.com.alfie.ecomm.feature.webview

import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.core.environment.model.Environment
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class WebViewDeeplinksTest {

    @RelaxedMockK
    private lateinit var environmentManager: EnvironmentManager

    @InjectMockKs
    private lateinit var webViewDeeplinks: WebViewDeeplinks

    @BeforeEach
    fun setup() {
        coEvery { environmentManager.current() } returns Environment.Prod(graphQLUrl = "", webUrl = "https://www.alfie.com")
    }

    @Test
    fun testSuccessfullyMatchesReturnOptionsDeeplink() {
        val url = "https://www.alfie.com/return-options"

        val result = webViewDeeplinks.interpreters[0].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }

    @Test
    fun testSuccessfullyMatchesPaymentOptionsDeeplink() {
        val url = "https://www.alfie.com/payment-options"

        val result = webViewDeeplinks.interpreters[1].specs.any { it.resolve(url) != null }

        assertTrue(result)
    }
}
