package au.com.alfie.ecomm.feature.webview.component

import android.os.Bundle
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import au.com.alfie.ecomm.feature.webview.component.LoadingState.Initializing

private const val STATE_BUNDLE_KEY = "bundle"

internal val WebStateSaver: Saver<WebViewState, Any> = run {

    mapSaver(
        save = {
            val viewState = Bundle().apply { it.webView?.saveState(this) }
            mapOf(STATE_BUNDLE_KEY to viewState)
        },
        restore = {
            WebViewState(WebContent.NavigatorOnly).apply {
                this.viewState = it[STATE_BUNDLE_KEY] as? Bundle
            }
        }
    )
}

@Composable
internal fun rememberWebViewState(webContent: WebContent): WebViewState = rememberSaveable(saver = WebStateSaver) {
    WebViewState(webContent)
}

@Stable
internal class WebViewState(webContent: WebContent) {

    var content by mutableStateOf(webContent)

    var webView by mutableStateOf<WebView?>(null)

    var viewState: Bundle? = null
        internal set

    var loadingState: LoadingState by mutableStateOf(Initializing)
        internal set
}
