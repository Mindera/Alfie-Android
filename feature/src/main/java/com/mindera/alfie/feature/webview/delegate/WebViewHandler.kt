package com.mindera.alfie.feature.webview.delegate

import androidx.lifecycle.ViewModel
import com.mindera.alfie.feature.webview.WebViewEvent

interface WebViewHandler {

    fun ViewModel.handleWebViewEvent(event: WebViewEvent)
}
