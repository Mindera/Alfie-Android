package au.com.alfie.ecomm.repository.shared.model

sealed class Media(
    val mediaContentType: MediaContentType,
    open val alt: String?
) {

    data class Image(
        override val alt: String? = null,
        val height: Int,
        val url: String,
        val width: Int
    ) : Media(
        mediaContentType = MediaContentType.IMAGE,
        alt = alt
    )

    data class Video(
        override val alt: String? = null,
        val previewImage: Image?,
        val sources: List<VideoSource>
    ) : Media(
        mediaContentType = MediaContentType.VIDEO,
        alt = alt
    )
}
