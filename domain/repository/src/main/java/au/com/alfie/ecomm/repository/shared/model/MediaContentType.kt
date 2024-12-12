package au.com.alfie.ecomm.repository.shared.model

enum class MediaContentType(val value: String) {
    IMAGE(value = "IMAGE"),
    VIDEO(value = "VIDEO"),
    UNKNOWN(value = "UNKNOWN__");

    companion object {
        fun fromValue(value: String) = entries.firstOrNull { it.value == value } ?: UNKNOWN
    }
}
