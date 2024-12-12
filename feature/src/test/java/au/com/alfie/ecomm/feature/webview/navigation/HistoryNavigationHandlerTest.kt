package au.com.alfie.ecomm.feature.webview.navigation

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HistoryNavigationHandlerTest {

    private val handler = HistoryNavigationHandler(UrlNavigationRoute())

    @Test
    fun `result -  when it does not match then returns Continue`() {
        val result = handler.resolve(
            fromUrl = "",
            toUrl = ""
        )

        assertEquals(HistoryNavigationResult.Continue, result)
    }

    @Test
    fun `result - when it matches both fromUrl and toUrl then returns result`() {
        val routes = spyk<UrlNavigationRoute>(recordPrivateCalls = true)
        every { routes["get"]() } returns linkedMapOf(
            NavigationRoute(
                from = Regex("a"),
                to = Regex("b")
            ) to HistoryNavigationResult.Close
        )
        val handler = HistoryNavigationHandler(routes)

        val result = handler.resolve(
            fromUrl = "a",
            toUrl = "b"
        )

        assertEquals(HistoryNavigationResult.Close, result)
    }

    @Test
    fun `result - when it matches only fromUrl then returns Continue`() {
        val routes = spyk<UrlNavigationRoute>(recordPrivateCalls = true)
        every { routes["get"]() } returns linkedMapOf(
            NavigationRoute(
                from = Regex("a"),
                to = Regex("b")
            ) to HistoryNavigationResult.Close
        )
        val handler = HistoryNavigationHandler(routes)

        val result = handler.resolve(
            fromUrl = "a",
            toUrl = "c"
        )

        assertEquals(HistoryNavigationResult.Continue, result)
    }

    @Test
    fun `result - when it matches only toUrl then returns Continue`() {
        val routes = spyk<UrlNavigationRoute>(recordPrivateCalls = true)
        every { routes["get"]() } returns linkedMapOf(
            NavigationRoute(
                from = Regex("a"),
                to = Regex("b")
            ) to HistoryNavigationResult.Close
        )
        val handler = HistoryNavigationHandler(routes)

        val result = handler.resolve(
            fromUrl = "abc",
            toUrl = "b"
        )

        assertEquals(HistoryNavigationResult.Continue, result)
    }
}
