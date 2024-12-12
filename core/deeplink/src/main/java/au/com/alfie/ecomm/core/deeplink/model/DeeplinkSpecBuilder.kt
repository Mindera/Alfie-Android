package au.com.alfie.ecomm.core.deeplink.model

fun deeplinkSpec(builder: DeeplinkSpecBuilder.() -> Unit): DeeplinkSpec =
    DeeplinkSpecBuilder().apply(builder).build()

class DeeplinkSpecBuilder {

    private var pathSegments: MutableList<DeeplinkPathSegment> = mutableListOf()

    private var queryParameters: MutableList<String> = mutableListOf()

    fun appendPathSegment(segment: DeeplinkPathSegment) {
        pathSegments.add(segment)
    }

    fun appendFixedPathSegment(segment: String) {
        pathSegments.add(DeeplinkPathSegment.Fixed(value = segment))
    }

    fun appendPatternPathSegment(regex: Regex) {
        pathSegments.add(DeeplinkPathSegment.Pattern(regex = regex))
    }

    fun appendArgumentPathSegment(
        argumentName: String,
        pattern: Regex? = null
    ) {
        pathSegments.add(
            DeeplinkPathSegment.Argument(
                name = argumentName,
                pattern = pattern
            )
        )
    }

    fun appendPathSegments(vararg segments: DeeplinkPathSegment) {
        pathSegments.addAll(segments)
    }

    fun appendPathSegments(segments: List<DeeplinkPathSegment>) {
        pathSegments.addAll(segments)
    }

    fun appendQueryParameter(key: String) {
        queryParameters.add(key)
    }

    fun appendQueryParameters(vararg keys: String) {
        queryParameters.addAll(keys)
    }

    fun appendQueryParameters(keys: List<String>) {
        queryParameters.addAll(keys)
    }

    internal fun build() = DeeplinkSpec(
        pathSegments = pathSegments,
        queryParameters = queryParameters
    )
}
