package com.mindera.alfie.feature.pdp.component

import androidx.compose.ui.graphics.Color
import com.mindera.alfie.designsystem.component.swatch.SwatchType

/**
 * Resolves a swatch to its fill colour — image swatches and no-swatch-at-all render
 * transparent so their container's surface shows through.
 */
internal fun SwatchType?.swatchColor(): Color = when (this) {
    is SwatchType.PlainColor -> color
    is SwatchType.Image -> Color.Transparent
    null -> Color.Transparent
}
