package au.com.alfie.ecomm.feature.webview.delegate

import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptionsBuilder
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import au.com.alfie.ecomm.feature.webview.WebViewEvent
import au.com.alfie.ecomm.feature.webview.navigation.HistoryNavigationHandler
import au.com.alfie.ecomm.feature.webview.navigation.HistoryNavigationResult
import au.com.alfie.ecomm.feature.webview.navigation.HistoryUpdate
import com.ramcosta.composedestinations.spec.Direction
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class WebViewHandlerDelegateTest {

    @RelaxedMockK
    private lateinit var historyNavigationHandler: HistoryNavigationHandler

    @RelaxedMockK
    private lateinit var deeplinkHandler: DeeplinkHandler

    @RelaxedMockK
    private lateinit var uiEventEmitterDelegate: UIEventEmitterDelegate

    @InjectMockKs
    private lateinit var delegate: WebViewHandlerDelegate

    @Test
    fun `handleWebViewEvent - given Close event then navigate back`() = runTest {
        val event = WebViewEvent.Close
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            handleWebViewEvent(event)

            coVerify { navigateBack() }
        }
    }

    @Test
    fun `handleWebViewEvent - given NavigateTo event with NavigateTo result then navigate to the direction`() = runTest {
        val direction = mockk<Direction>()
        val navOptions: NavOptionsBuilder.() -> Unit = { }
        val result = DeeplinkResult.NavigateTo(direction, navOptions)
        val event = WebViewEvent.NavigateTo(result)
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            handleWebViewEvent(event)

            coVerify { navigateTo(direction, navOptions) }
        }
    }

    @Test
    fun `handleWebViewEvent - given NavigateTo event with NavigateClearingStack result then navigate to the direction`() = runTest {
        val direction = mockk<Direction>()
        val result = DeeplinkResult.NavigateClearingStack(direction)
        val event = WebViewEvent.NavigateTo(result)
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            handleWebViewEvent(event)

            coVerify { navigateClearingStack(direction) }
        }
    }

    @Test
    fun `handleWebViewEvent - given NavigateTo event with Unresolved result then do not navigate`() = runTest {
        val result = DeeplinkResult.Unresolved("")
        val event = WebViewEvent.NavigateTo(result)
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            handleWebViewEvent(event)

            coVerify(exactly = 0) { emitUIEvent(any()) }
        }
    }

    @Test
    fun `handleWebViewEvent - given OnHistoryUpdate event with Close result then navigate back`() = runTest {
        val historyUpdate = HistoryUpdate("to.url", "from.url") {}
        val event = WebViewEvent.OnHistoryUpdate(historyUpdate)
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        coEvery { historyNavigationHandler.resolve("to.url", "from.url") } returns HistoryNavigationResult.Close

        with(viewModel) {
            handleWebViewEvent(event)

            coVerify { navigateBack() }
        }
    }

    @Test
    fun `handleWebViewEvent - given OnHistoryUpdate event with Continue result and valid deeplink then navigate to the deeplink`() = runTest {
        val historyUpdate = HistoryUpdate("to.url", "from.url") {}
        val event = WebViewEvent.OnHistoryUpdate(historyUpdate)
        val direction = mockk<Direction>()
        val navOptions: NavOptionsBuilder.() -> Unit = { }
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        every { direction.route } returns "another_screen"
        coEvery { historyNavigationHandler.resolve("to.url", "from.url") } returns HistoryNavigationResult.Continue
        coEvery { deeplinkHandler.resolve("to.url") } returns DeeplinkResult.NavigateTo(direction, navOptions)

        with(viewModel) {
            handleWebViewEvent(event)

            coVerify { navigateTo(direction, navOptions) }
        }
    }

    @Test
    fun `handleWebViewEvent - given OnHistoryUpdate event with Continue result and not valid deeplink then do not navigate`() = runTest {
        val historyUpdate = HistoryUpdate("to.url", "from.url") {}
        val event = WebViewEvent.OnHistoryUpdate(historyUpdate)
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        coEvery { historyNavigationHandler.resolve("to.url", "from.url") } returns HistoryNavigationResult.Continue
        coEvery { deeplinkHandler.resolve("to.url") } returns DeeplinkResult.Unresolved("")

        with(viewModel) {
            handleWebViewEvent(event)

            coVerify(exactly = 0) { emitUIEvent(any()) }
        }
    }

    @Test
    fun `handleWebViewEvent - given OnHistoryUpdate event with Continue result and WebView deeplink then do not navigate`() = runTest {
        val historyUpdate = HistoryUpdate("to.url", "from.url") {}
        val event = WebViewEvent.OnHistoryUpdate(historyUpdate)
        val direction = mockk<Direction>()
        val navOptions: NavOptionsBuilder.() -> Unit = { }
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        every { direction.route } returns "web_view"
        coEvery { historyNavigationHandler.resolve("to.url", "from.url") } returns HistoryNavigationResult.Continue
        coEvery { deeplinkHandler.resolve("to.url") } returns DeeplinkResult.NavigateTo(direction, navOptions)

        with(viewModel) {
            handleWebViewEvent(event)

            coVerify(exactly = 0) { emitUIEvent(any()) }
        }
    }

    private class TestViewModel(
        webViewHandlerDelegate: WebViewHandlerDelegate,
        uiEventEmitterDelegate: UIEventEmitterDelegate
    ) : ViewModel(), WebViewHandler by webViewHandlerDelegate, UIEventEmitter by uiEventEmitterDelegate
}
