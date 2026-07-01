package com.mindera.alfie.designsystem.tokens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
class NewTheme(
    val primitive: Primitives = LightPrimitives,
    val color: Colors = DefaultColors(primitive),
    val sizing: Sizing = DefaultSizing(primitive),
    val typographyTokens: TypographyTokens = DefaultTypographyTokens(primitive),
    val typography: Typography = DefaultTypography(typographyTokens)
) {
    val spacing: PrimitiveSpacing get() = primitive.spacing
}

val LocalTheme = staticCompositionLocalOf { NewTheme() }

@Suppress("ComposableFunctionName")
@Composable
fun ProvideNewTheme(content: @Composable () -> Unit) {
    val providedTheme = remember { NewTheme() }
    CompositionLocalProvider(LocalTheme provides providedTheme, content = content)
}
