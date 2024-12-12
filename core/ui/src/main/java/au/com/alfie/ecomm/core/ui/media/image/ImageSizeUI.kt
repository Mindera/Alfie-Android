package au.com.alfie.ecomm.core.ui.media.image

import androidx.compose.runtime.Stable

private const val X_SMALL_WIDTH: Int = 100
private const val SMALL_WIDTH: Int = 200
private const val MEDIUM_WIDTH: Int = 500
private const val LARGE_WIDTH: Int = 1024

sealed class ImageSizeUI(
    open val url: String,
    open val width: Int
) {

    @Stable
    data class XSmall(override val url: String) : ImageSizeUI(url, X_SMALL_WIDTH)

    @Stable
    data class Small(override val url: String) : ImageSizeUI(url, SMALL_WIDTH)

    @Stable
    data class Medium(override val url: String) : ImageSizeUI(url, MEDIUM_WIDTH)

    @Stable
    data class Large(override val url: String) : ImageSizeUI(url, LARGE_WIDTH)

    @Stable
    data class Custom(
        override val url: String,
        override val width: Int
    ) : ImageSizeUI(url, width)
}
