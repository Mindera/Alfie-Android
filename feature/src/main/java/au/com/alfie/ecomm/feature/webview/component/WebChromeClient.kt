package au.com.alfie.ecomm.feature.webview.component

import android.webkit.WebChromeClient
import android.webkit.WebView
import au.com.alfie.ecomm.feature.webview.component.LoadingState.Finished

internal class WebChromeClient : WebChromeClient() {

    companion object {
        private const val FULL_PROGRESS = 100F
    }

    var state: WebViewState? = null
        internal set

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        if (state?.loadingState == Finished) return
        state?.loadingState = LoadingState.Loading(progress = newProgress / FULL_PROGRESS)
    }
}
