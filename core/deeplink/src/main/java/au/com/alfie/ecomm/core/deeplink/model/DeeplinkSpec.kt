package au.com.alfie.ecomm.core.deeplink.model

import android.net.Uri

data class DeeplinkSpec(
    val pathSegments: List<DeeplinkPathSegment>,
    val queryParameters: List<String>
) {

    fun resolve(url: String): DeeplinkInstance? {
        val uri = Uri.parse(url)

        if (uri.isHierarchical.not() || pathSegments.size != uri.pathSegments.size) return null

        val pathArgs = mutableMapOf<String, String>()
        uri.pathSegments.forEachIndexed { index, urlSegment ->
            when (val deeplinkSegment = pathSegments[index]) {
                is DeeplinkPathSegment.Fixed -> {
                    if (urlSegment != deeplinkSegment.value) return null
                }
                is DeeplinkPathSegment.Pattern -> {
                    if (!urlSegment.matches(deeplinkSegment.regex)) return null
                }
                is DeeplinkPathSegment.Argument -> {
                    if (deeplinkSegment.pattern != null && !urlSegment.matches(deeplinkSegment.pattern)) {
                        return null
                    }
                    pathArgs[deeplinkSegment.name] = urlSegment
                }
            }
        }
        val queryArgs: Map<String, String> = buildMap {
            queryParameters.forEach { param ->
                runCatching { uri.getQueryParameter(param) }.getOrNull()?.let { put(param, it) }
            }
        }

        return DeeplinkInstance(
            spec = this,
            pathArguments = pathArgs,
            queryArguments = queryArgs,
            namedAnchor = uri.fragment
        )
    }
}
