package au.com.alfie.ecomm.feature.webview

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.navigation.arguments.webview.WebViewNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.webview.webViewNavArgs
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import au.com.alfie.ecomm.feature.webview.delegate.WebViewHandlerDelegate
import au.com.alfie.ecomm.feature.webview.factory.WebViewUIFactory
import au.com.alfie.ecomm.feature.webview.model.WebViewUIState
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class WebViewViewModelTest {

    @RelaxedMockK
    private lateinit var uiFactory: WebViewUIFactory

    @RelaxedMockK
    private lateinit var deeplinkHandler: DeeplinkHandler

    @RelaxedMockK
    private lateinit var savedStateHandle: SavedStateHandle

    @RelaxedMockK
    private lateinit var webViewHandlerDelegate: WebViewHandlerDelegate

    @BeforeEach
    fun setup() {
        mockkStatic("au.com.alfie.ecomm.feature.webview.NavArgsGettersKt")
        every { savedStateHandle.navArgs<WebViewNavArgs>() } returns webViewNavArgs(url = "https://www.alfie.com")
    }

    @Test
    fun `GIVEN success getting content THEN emit state Loaded`() = runTest {
        val subject = createViewModel()

        subject.state.test {
            awaitItem()
            assertIs<WebViewUIState.Loaded>(subject.state.value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `GIVEN error getting content THEN emit state Error`() = runTest {
        every { savedStateHandle.navArgs<WebViewNavArgs>() } returns webViewNavArgs(url = "")

        val subject = createViewModel()

        subject.state.test {
            awaitItem()
            assertIs<WebViewUIState.Error>(subject.state.value)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel() =
        WebViewViewModel(
            uiFactory,
            deeplinkHandler,
            savedStateHandle,
            UIEventEmitterDelegate(),
            webViewHandlerDelegate
        )
}
