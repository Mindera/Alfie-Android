package com.mindera.alfie.feature.webview.navigation

internal sealed interface HistoryNavigationResult {

    data object Continue : HistoryNavigationResult

    data object Close : HistoryNavigationResult
}
