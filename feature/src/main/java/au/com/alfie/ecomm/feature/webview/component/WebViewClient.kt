package au.com.alfie.ecomm.feature.webview.component

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import au.com.alfie.ecomm.feature.webview.component.LoadingState.Finished
import kotlinx.coroutines.runBlocking

internal class WebViewClient : WebViewClient() {

    var state: WebViewState? = null
        internal set

    var navigator: WebViewNavigator? = null
        internal set

    override fun onPageFinished(
        view: WebView,
        url: String
    ) {
        super.onPageFinished(view, url)
        state?.loadingState = Finished
    }

    override fun doUpdateVisitedHistory(
        view: WebView,
        url: String,
        isReload: Boolean
    ) {
        super.doUpdateVisitedHistory(view, url, isReload)
        navigator?.onUrlChange(url)
        navigator?.canGoBack = view.canGoBack()
        navigator?.canGoForward = view.canGoForward()
    }

    override fun shouldOverrideUrlLoading(
        view: WebView,
        request: WebResourceRequest
    ): Boolean {
        val url = request.url.toString()
        val canOverride = runBlocking { navigator?.canOverride(url) ?: false }
        if (canOverride) navigator?.overrideUrl(url)
        return canOverride
    }
}
