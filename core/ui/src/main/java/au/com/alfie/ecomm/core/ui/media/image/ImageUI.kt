package au.com.alfie.ecomm.core.ui.media.image

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.core.ui.media.MediaUI
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.abs

@Stable
data class ImageUI(
    val images: ImmutableList<ImageSizeUI>,
    val alt: String?
) : MediaUI

@Stable
fun ImageUI.pickImageUrlBySize(width: Int): String = images.minByOrNull { abs(width - it.width) }?.url.orEmpty()
