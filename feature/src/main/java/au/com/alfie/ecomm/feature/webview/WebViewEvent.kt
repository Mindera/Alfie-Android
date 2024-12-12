package au.com.alfie.ecomm.feature.webview

import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.feature.webview.navigation.HistoryUpdate

sealed interface WebViewEvent {

    data class NavigateTo(val result: DeeplinkResult) : WebViewEvent

    data class OnHistoryUpdate(val result: HistoryUpdate) : WebViewEvent

    data object Close : WebViewEvent
}
