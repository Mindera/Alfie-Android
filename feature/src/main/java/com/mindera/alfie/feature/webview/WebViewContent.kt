package com.mindera.alfie.feature.webview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mindera.alfie.core.deeplink.DeeplinkHandler
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.dialog.error.ErrorScreen
import com.mindera.alfie.designsystem.component.dialog.error.ErrorType
import com.mindera.alfie.feature.webview.WebViewEvent.Close
import com.mindera.alfie.feature.webview.WebViewEvent.NavigateTo
import com.mindera.alfie.feature.webview.WebViewEvent.OnHistoryUpdate
import com.mindera.alfie.feature.webview.component.WebContent
import com.mindera.alfie.feature.webview.component.WebView
import com.mindera.alfie.feature.webview.component.rememberWebViewNavigator
import com.mindera.alfie.feature.webview.component.rememberWebViewState

@Composable
fun WebViewContent(
    url: String,
    queryParameters: Map<String, String>,
    headers: Map<String, String>,
    deeplinkHandler: DeeplinkHandler,
    onEvent: ClickEventOneArg<WebViewEvent>,
    errorType: ErrorType,
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
            ErrorScreen(errorType.copy(onButtonClick = {
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
