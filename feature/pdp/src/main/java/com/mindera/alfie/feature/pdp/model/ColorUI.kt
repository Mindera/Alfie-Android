package com.mindera.alfie.feature.pdp.model

import androidx.compose.runtime.Stable
import com.mindera.alfie.designsystem.component.swatch.SwatchType

@Stable
internal data class ColorUI(
    val id: String,
    val type: SwatchType,
    val index: Int,
    val isSelected: Boolean = false
)
