package au.com.alfie.ecomm.feature.webview.factory

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class WebViewUIFactoryTest {

    @InjectMockKs
    private lateinit var subject: WebViewUIFactory

    @Test
    fun `invoke - given a bag return the UI model`() = runTest {
        val expectedUrl = "url"
        val queryParameters = mapOf("queryKey" to "queryValue")
        val defaultHeaders = mapOf("headerKey" to "headerValue")
        val result = subject(
            expectedUrl,
            queryParameters,
            defaultHeaders
        )

        assertEquals(result.url, expectedUrl)
        assertEquals(result.queryParameters, queryParameters)
        assertEquals(result.headers, defaultHeaders)
    }
}
