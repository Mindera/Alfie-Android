package com.mindera.alfie.core.ui.media.image

import androidx.compose.runtime.Stable
import com.mindera.alfie.core.ui.media.MediaUI
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.abs

@Stable
data class ImageUI(
    val images: ImmutableList<ImageSizeUI>,
    val alt: String?
) : MediaUI {
    companion object {
        fun preview(
            url: String = "https://images.pexels.com/photos/9362029/pexels-photo-9362029.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        ) = ImageUI(
            images = persistentListOf(ImageSizeUI.Large(url)),
            alt = ""
        )
    }
}

@Stable
fun ImageUI.pickImageUrlBySize(width: Int): String = images.minByOrNull { abs(width - it.width) }?.url.orEmpty()
