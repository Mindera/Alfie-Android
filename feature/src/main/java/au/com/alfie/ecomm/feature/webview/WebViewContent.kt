package au.com.alfie.ecomm.feature.webview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.feature.webview.WebViewEvent.Close
import au.com.alfie.ecomm.feature.webview.WebViewEvent.NavigateTo
import au.com.alfie.ecomm.feature.webview.WebViewEvent.OnHistoryUpdate
import au.com.alfie.ecomm.feature.webview.component.WebContent
import au.com.alfie.ecomm.feature.webview.component.WebView
import au.com.alfie.ecomm.feature.webview.component.rememberWebViewNavigator
import au.com.alfie.ecomm.feature.webview.component.rememberWebViewState

@Composable
fun WebViewContent(
    url: String,
    queryParameters: Map<String, String>,
    headers: Map<String, String>,
    deeplinkHandler: DeeplinkHandler,
    onEvent: ClickEventOneArg<WebViewEvent>,
    modifier: Modifier = Modifier,
    isBackHandlerEnabled: Boolean = true
) {
    val content = WebContent.Url(
        url = url,
        queryParameters = queryParameters,
        httpHeaders = headers
    )
    val webViewState = rememberWebViewState(content)
    val navigator = rememberWebViewNavigator(deeplinkHandler)

    Surface(modifier = modifier) {
        WebView(
            state = webViewState,
            onLoadingOverride = { onEvent(NavigateTo(it)) },
            onHistoryUpdate = { onEvent(OnHistoryUpdate(it)) },
            onClose = { onEvent(Close) },
            navigator = navigator,
            isBackHandlerEnabled = isBackHandlerEnabled
        )
    }
}
