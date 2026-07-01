package com.mindera.alfie.designsystem.component.loading

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mindera.alfie.designsystem.tokens.LocalTheme

enum class LoadingType {
    Dark,
    Light
}

@Composable
fun LoadingType.color(): Color {
    val c = LocalTheme.current.primitive.colors
    return when (this) {
        LoadingType.Dark -> c.neutrals800
        LoadingType.Light -> c.neutrals0
    }
}
