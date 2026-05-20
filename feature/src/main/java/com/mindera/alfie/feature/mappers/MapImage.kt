package com.mindera.alfie.feature.mappers

import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.repository.shared.model.Media
import kotlinx.collections.immutable.persistentListOf

fun Media.Image?.toImageUI(): ImageUI {
    val imageSizeUI = ImageSizeUI.Custom(
        url = this?.url.orEmpty()
    )
    return ImageUI(
        images = persistentListOf(imageSizeUI),
        alt = this?.alt
    )
}