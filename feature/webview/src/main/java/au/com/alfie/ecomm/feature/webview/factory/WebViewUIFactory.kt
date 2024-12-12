package au.com.alfie.ecomm.feature.webview.factory

import au.com.alfie.ecomm.feature.webview.model.WebViewUI
import javax.inject.Inject

internal class WebViewUIFactory @Inject constructor() {

    operator fun invoke(
        url: String,
        queryParameters: Map<String, String>,
        headers: Map<String, String>
    ): WebViewUI =
        WebViewUI(
            url = url,
            queryParameters = queryParameters,
            headers = headers
        )
}
