package com.mindera.alfie.designsystem.component.loading

import androidx.compose.ui.graphics.Color
import com.mindera.alfie.designsystem.theme.Theme

enum class LoadingType(
    val color: Color
) {
    Dark(color = Theme.color.primary.mono900),
    Light(color = Theme.color.white)
}
