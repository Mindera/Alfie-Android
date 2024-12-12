package au.com.alfie.ecomm.core.ui.media.video

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.core.ui.media.MediaUI

@Stable
data class VideoUI(
    val previewImage: VideoPreviewImageUI,
    val source: VideoSourceUI
) : MediaUI
