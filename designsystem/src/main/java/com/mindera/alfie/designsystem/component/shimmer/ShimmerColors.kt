package com.mindera.alfie.designsystem.component.shimmer

import androidx.compose.runtime.Composable
import com.mindera.alfie.designsystem.tokens.LocalTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

enum class ShimmerColors {
    Light,
    Dark
}

@Composable
fun ShimmerColors.resolvedColors(): ImmutableList<androidx.compose.ui.graphics.Color> {
    val c = LocalTheme.current.primitive.colors
    return when (this) {
        ShimmerColors.Light -> persistentListOf(c.neutrals200, c.neutrals300, c.neutrals200)
        ShimmerColors.Dark -> persistentListOf(c.neutrals600, c.neutrals800, c.neutrals600)
    }
}
