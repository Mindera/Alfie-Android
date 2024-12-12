package au.com.alfie.ecomm.feature.webview.navigation

internal sealed interface HistoryNavigationResult {

    data object Continue : HistoryNavigationResult

    data object Close : HistoryNavigationResult
}
