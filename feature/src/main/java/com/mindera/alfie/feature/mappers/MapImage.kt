package com.mindera.alfie.feature.mappers

import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.product.model.Variant
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

/**
 * A product card's image for [variant]: its own first image, else the product's. Variants often carry
 * no media of their own, and this is the fallback the PDP gallery applies too — without it the card
 * draws the empty-image placeholder.
 */
fun Product.cardImageFor(variant: Variant?): ImageUI =
    (variant?.media?.firstOrNull() ?: images.firstOrNull()).toImageUI()