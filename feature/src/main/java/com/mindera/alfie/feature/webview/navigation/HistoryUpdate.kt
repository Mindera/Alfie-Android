package com.mindera.alfie.feature.webview.navigation

data class HistoryUpdate(
    val to: String,
    val from: String,
    val cancelUpdate: () -> Unit
)
