package au.com.alfie.ecomm.core.deeplink.model

sealed interface DeeplinkPathSegment {

    data class Fixed(val value: String) : DeeplinkPathSegment

    data class Pattern(val regex: Regex) : DeeplinkPathSegment

    data class Argument(
        val name: String,
        val pattern: Regex? = null
    ) : DeeplinkPathSegment
}
