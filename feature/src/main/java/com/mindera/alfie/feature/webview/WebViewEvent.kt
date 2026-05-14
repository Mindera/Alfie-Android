package com.mindera.alfie.feature.webview

import com.mindera.alfie.core.deeplink.DeeplinkResult
import com.mindera.alfie.feature.webview.navigation.HistoryUpdate

sealed interface WebViewEvent {

    data class NavigateTo(val result: DeeplinkResult) : WebViewEvent

    data class OnHistoryUpdate(val result: HistoryUpdate) : WebViewEvent

    data object Close : WebViewEvent
}
