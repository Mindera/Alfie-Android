package com.mindera.alfie.feature.webview.navigation

import com.mindera.alfie.feature.webview.navigation.HistoryNavigationResult.Continue
import javax.inject.Inject

internal class UrlNavigationRoute @Inject constructor() {

    fun result(
        fromUrl: String,
        toUrl: String,
        default: HistoryNavigationResult = Continue
    ): HistoryNavigationResult = findMatchingRoute(fromUrl, toUrl) ?: default

    private fun get(): HashMap<NavigationRoute, HistoryNavigationResult> = linkedMapOf()

    private fun findMatchingRoute(
        fromUrl: String,
        toUrl: String
    ): HistoryNavigationResult? =
        get().entries.firstNotNullOfOrNull { (navigationRoute, result) ->
            if (toUrl.matches(navigationRoute.to) && fromUrl.matches(navigationRoute.from)) {
                result
            } else {
                null
            }
        }
}
