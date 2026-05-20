package com.mindera.alfie.core.ui.media.video

import androidx.compose.runtime.Stable
import com.mindera.alfie.core.ui.media.MediaUI

@Stable
data class VideoUI(
    val previewImage: VideoPreviewImageUI,
    val source: VideoSourceUI
) : MediaUI
