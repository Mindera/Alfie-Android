package au.com.alfie.ecomm.feature.webview

import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.core.deeplink.DeeplinkInterpreter
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkInstance
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkSpec
import au.com.alfie.ecomm.core.deeplink.model.deeplinkSpec
import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.core.navigation.arguments.webview.webViewNavArgs
import au.com.alfie.ecomm.feature.webview.destinations.WebViewScreenDestination
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class WebViewDeeplinks @Inject constructor(
    private val environmentManager: EnvironmentManager
) : DeeplinkGroup {

    companion object {
        private const val RETURN_OPTIONS_SEGMENT = "return-options"
        private const val PAYMENT_OPTIONS_SEGMENT = "payment-options"
    }

    // Deeplinks for WebView screens that require custom arguments (e.g. TopBar parameters)
    override val interpreters: List<DeeplinkInterpreter> = listOf(
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/return-options
                deeplinkSpec {
                    appendFixedPathSegment(RETURN_OPTIONS_SEGMENT)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult {
                val environment = environmentManager.current()
                return DeeplinkResult.NavigateTo(
                    direction = WebViewScreenDestination(
                        webViewNavArgs(
                            url = "${environment.webUrl}/return-options",
                            title = StringResource.fromId(R.string.delivery_and_returns_title)
                        )
                    )
                )
            }
        },
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/payment-options
                deeplinkSpec {
                    appendFixedPathSegment(PAYMENT_OPTIONS_SEGMENT)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult {
                val environment = environmentManager.current()
                return DeeplinkResult.NavigateTo(
                    direction = WebViewScreenDestination(
                        webViewNavArgs(
                            url = "${environment.webUrl}/payment-options",
                            title = StringResource.fromId(R.string.payment_options_title)
                        )
                    )
                )
            }
        }
    )
}
