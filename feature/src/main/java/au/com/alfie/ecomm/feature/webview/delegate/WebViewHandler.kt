package au.com.alfie.ecomm.feature.webview.delegate

import androidx.lifecycle.ViewModel
import au.com.alfie.ecomm.feature.webview.WebViewEvent

interface WebViewHandler {

    fun ViewModel.handleWebViewEvent(event: WebViewEvent)
}
