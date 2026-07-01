package com.mindera.alfie.designsystem.component.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mindera.alfie.designsystem.component.loading.LoadingType
import com.mindera.alfie.designsystem.component.loading.LoadingType.Dark
import com.mindera.alfie.designsystem.component.loading.LoadingType.Light
import com.mindera.alfie.designsystem.component.shimmer.ShimmerColors
import com.mindera.alfie.designsystem.tokens.LocalTheme

@Immutable
data class ButtonColorSpec(
    val background: Color,
    val text: Color,
    val icon: Color,
    val border: Color,
    val disabledBackground: Color,
    val disabledText: Color,
    val disabledIcon: Color,
    val disabledBorder: Color
)

@Composable
fun ButtonType.colorSpec(): ButtonColorSpec {
    val c = LocalTheme.current.primitive.colors
    return when (this) {
        ButtonType.Primary -> ButtonColorSpec(
            background = c.neutrals800,
            text = c.neutrals0,
            icon = c.neutrals0,
            border = c.neutrals800,
            disabledBackground = c.neutrals300,
            disabledText = c.neutrals500,
            disabledIcon = c.neutrals500,
            disabledBorder = c.neutrals300
        )
        ButtonType.Secondary -> ButtonColorSpec(
            background = c.transparent,
            text = c.neutrals800,
            icon = c.neutrals900,
            border = c.neutrals900,
            disabledBackground = c.transparent,
            disabledText = c.neutrals500,
            disabledIcon = c.neutrals500,
            disabledBorder = c.neutrals500
        )
        ButtonType.Tertiary -> ButtonColorSpec(
            background = c.transparent,
            text = c.neutrals800,
            icon = c.neutrals900,
            border = c.transparent,
            disabledBackground = c.transparent,
            disabledText = c.neutrals400,
            disabledIcon = c.neutrals500,
            disabledBorder = c.transparent
        )
        ButtonType.Destructive -> ButtonColorSpec(
            background = c.semanticError600,
            text = c.neutrals0,
            icon = c.neutrals0,
            border = c.semanticError600,
            disabledBackground = c.neutrals300,
            disabledText = c.neutrals500,
            disabledIcon = c.neutrals500,
            disabledBorder = c.neutrals300
        )
        ButtonType.Underlined -> ButtonColorSpec(
            background = c.transparent,
            text = c.neutrals800,
            icon = c.neutrals800,
            border = c.transparent,
            disabledBackground = c.transparent,
            disabledText = c.neutrals400,
            disabledIcon = c.neutrals400,
            disabledBorder = c.transparent
        )
    }
}

enum class ButtonType(
    val hasBorder: Boolean,
    val loadingType: LoadingType,
    val disabledLoadingType: LoadingType,
    val shimmerColors: ShimmerColors = ShimmerColors.Light
) {
    Primary(hasBorder = false, loadingType = Light, disabledLoadingType = Dark, shimmerColors = ShimmerColors.Dark),
    Secondary(hasBorder = true, loadingType = Dark, disabledLoadingType = Dark),
    Tertiary(hasBorder = false, loadingType = Dark, disabledLoadingType = Dark),
    Destructive(hasBorder = false, loadingType = Light, disabledLoadingType = Dark, shimmerColors = ShimmerColors.Dark),
    Underlined(hasBorder = false, loadingType = Dark, disabledLoadingType = Dark)
}
