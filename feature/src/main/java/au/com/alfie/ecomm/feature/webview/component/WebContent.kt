package au.com.alfie.ecomm.feature.webview.component

internal sealed interface WebContent {

    data class Url(
        val url: String,
        val queryParameters: Map<String, String> = emptyMap(),
        val httpHeaders: Map<String, String> = emptyMap()
    ) : WebContent

    data class Post(
        val url: String,
        val data: ByteArray
    ) : WebContent {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Post

            if (url != other.url) return false
            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int = 31 * url.hashCode() + data.contentHashCode()
    }

    data object NavigatorOnly : WebContent
}
