package au.com.alfie.ecomm.repository.shared.model

data class VideoSource(
    val format: VideoFormat,
    val height: Int,
    val mimeType: String,
    val url: String,
    val width: Int
)
