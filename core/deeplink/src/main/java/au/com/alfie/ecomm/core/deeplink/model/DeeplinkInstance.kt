package au.com.alfie.ecomm.core.deeplink.model

data class DeeplinkInstance(
    val spec: DeeplinkSpec,
    val pathArguments: Map<String, String>,
    val queryArguments: Map<String, String>,
    val namedAnchor: String?
)
