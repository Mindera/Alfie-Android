package au.com.alfie.ecomm.feature.webview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.dialog.ErrorData
import au.com.alfie.ecomm.designsystem.component.dialog.ErrorScreen
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
    errorData: ErrorData,
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
    var isLoadFailed by remember { mutableStateOf(false) }

    Surface(modifier = modifier) {
        if (isLoadFailed) {
            ErrorScreen(errorData.copy(onButtonClick = {
                isLoadFailed = false
                navigator.reload(content.url)
            }))
        } else {
            WebView(
                state = webViewState,
                onLoadingOverride = { onEvent(NavigateTo(it)) },
                onHistoryUpdate = { onEvent(OnHistoryUpdate(it)) },
                onClose = { onEvent(Close) },
                navigator = navigator,
                isBackHandlerEnabled = isBackHandlerEnabled,
                onLoadFailure = {
                    isLoadFailed = true
                }
            )
        }
    }
}
