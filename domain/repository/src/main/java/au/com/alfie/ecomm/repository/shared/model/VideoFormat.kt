package au.com.alfie.ecomm.repository.shared.model

enum class VideoFormat(val value: String) {
    MP4(value = "MP4"),
    WEBM(value = "WEBM"),
    UNKNOWN(value = "UNKNOWN__");

    companion object {
        fun fromValue(value: String) = entries.firstOrNull { it.value == value } ?: UNKNOWN
    }
}
