package com.mindera.alfie.feature.webview.navigation

internal data class NavigationRoute(
    val from: Regex,
    val to: Regex
)
