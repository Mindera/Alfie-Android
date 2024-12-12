package au.com.alfie.ecomm.feature.webview.navigation

import javax.inject.Inject

internal class HistoryNavigationHandler @Inject constructor(
    private val routes: UrlNavigationRoute
) {

    fun resolve(
        toUrl: String,
        fromUrl: String
    ): HistoryNavigationResult = routes.result(
        fromUrl = fromUrl,
        toUrl = toUrl
    )
}
