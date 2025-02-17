package au.com.alfie.ecomm.feature.mappers

import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.repository.shared.model.Media
import kotlinx.collections.immutable.persistentListOf

fun Media.Image?.mapImage(): ImageUI {
    val imageSizeUI = ImageSizeUI.Custom(
        url = this?.url.orEmpty()
    )
    return ImageUI(
        images = persistentListOf(imageSizeUI),
        alt = this?.alt
    )
}