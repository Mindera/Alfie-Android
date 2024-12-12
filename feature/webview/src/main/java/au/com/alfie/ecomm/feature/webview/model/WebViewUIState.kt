package au.com.alfie.ecomm.feature.webview.model

internal sealed interface WebViewUIState {

    data object Loading : WebViewUIState

    data class Loaded(val content: WebViewUI) : WebViewUIState

    data object Error : WebViewUIState
}
