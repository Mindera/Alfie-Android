package com.mindera.alfie.feature.webview.model

internal data class WebViewUI(
    val url: String,
    val queryParameters: Map<String, String>,
    val headers: Map<String, String>
)
