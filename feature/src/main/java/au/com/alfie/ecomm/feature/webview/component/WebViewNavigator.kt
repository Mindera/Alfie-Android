package au.com.alfie.ecomm.feature.webview.component

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.feature.webview.component.NavigationEvent.LoadUrl
import au.com.alfie.ecomm.feature.webview.component.NavigationEvent.OnUrlChange
import au.com.alfie.ecomm.feature.webview.component.NavigationEvent.UrlLoadingOverride
import au.com.alfie.ecomm.feature.webview.navigation.HistoryUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal fun rememberWebViewNavigator(
    deeplinkHandler: DeeplinkHandler,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): WebViewNavigator = remember(coroutineScope) { WebViewNavigator(deeplinkHandler, coroutineScope) }

internal class WebViewNavigator(
    private val deeplinkHandler: DeeplinkHandler,
    private val coroutineScope: CoroutineScope
) {

    var canGoBack: Boolean by mutableStateOf(false)
        internal set

    var canGoForward: Boolean by mutableStateOf(false)
        internal set

    private val navigationEvents: MutableSharedFlow<NavigationEvent> = MutableSharedFlow(replay = 1)

    suspend fun handleNavigation(
        webView: WebView,
        onOverride: (DeeplinkResult) -> Unit,
        onHistoryUpdate: (HistoryUpdate) -> Unit
    ): Nothing = withContext(Dispatchers.Main) {
        navigationEvents.collect { event ->
            when (event) {
                is LoadUrl -> webView.loadUrl(event.url)
                is UrlLoadingOverride -> deeplinkHandler.resolve(event.url).let(onOverride)
                is OnUrlChange -> {
                    if (canGoBack) {
                        val history = webView.copyBackForwardList()
                        val previousHistoryIndex = history.currentIndex.dec()
                        val previousUrl = history.getItemAtIndex(previousHistoryIndex).url
                        val historyUpdate = HistoryUpdate(event.url, previousUrl) { webView.goBack() }
                        onHistoryUpdate(historyUpdate)
                    }
                }
            }
        }
    }

    suspend fun canOverride(url: String): Boolean {
        return when (val result = deeplinkHandler.resolve(url)) {
            is DeeplinkResult.NavigateTo -> result.direction.route.startsWith("web_view").not()
            is DeeplinkResult.NavigateClearingStack -> result.direction.route.startsWith("web_view").not()
            is DeeplinkResult.Unresolved -> false
        }
    }

    fun overrideUrl(url: String) {
        coroutineScope.launch {
            val event = UrlLoadingOverride(url)
            navigationEvents.emit(event)
        }
    }

    fun onUrlChange(url: String) {
        coroutineScope.launch {
            val event = OnUrlChange(url)
            navigationEvents.emit(event)
        }
    }

    fun reload(url: String) {
        coroutineScope.launch {
            navigationEvents.emit(LoadUrl(url))
        }
    }
}
