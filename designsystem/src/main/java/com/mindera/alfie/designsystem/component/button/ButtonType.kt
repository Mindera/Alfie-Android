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

/**
 * Button colours come from the theme's component tokens, not from the raw palette, so a brand swap
 * in the token layer reaches buttons without touching this file. The token set gives one `content`
 * token per variant, so text and icon share it.
 *
 * `terciary` is the design system's spelling of the token group; the [ButtonType] entry stays
 * `Tertiary`. [ButtonType.Underlined] has no `button-*` group and maps onto the `link` tokens.
 */
@Composable
fun ButtonType.colorSpec(): ButtonColorSpec {
    val theme = LocalTheme.current
    val button = theme.color.button
    val link = theme.color.link
    val transparent = theme.primitive.colors.transparent
    return when (this) {
        ButtonType.Primary -> ButtonColorSpec(
            background = button.primaryBackgroundPrimaryDefault,
            text = button.primaryContentPrimaryDefault,
            icon = button.primaryContentPrimaryDefault,
            border = button.primaryStrokePrimaryDefault,
            disabledBackground = button.primaryBackgroundPrimaryDisabled,
            disabledText = button.primaryContentPrimaryDisabled,
            disabledIcon = button.primaryContentPrimaryDisabled,
            disabledBorder = button.primaryStrokePrimaryDisabled
        )
        ButtonType.Secondary -> ButtonColorSpec(
            background = button.secondaryBackgroundSecondaryDefault,
            text = button.secondaryContentSecondaryDefault,
            icon = button.secondaryContentSecondaryDefault,
            border = button.secondaryStrokeSecondaryDefault,
            disabledBackground = button.secondaryBackgroundSecondaryDisabled,
            disabledText = button.secondaryContentSecondaryDisabled,
            disabledIcon = button.secondaryContentSecondaryDisabled,
            disabledBorder = button.secondaryStrokeSecondaryDisabled
        )
        ButtonType.Tertiary -> ButtonColorSpec(
            background = button.terciaryBackgroundTerciaryDefault,
            text = button.terciaryContentTerciaryDefault,
            icon = button.terciaryContentTerciaryDefault,
            border = button.terciaryStrokeTerciaryDefault,
            disabledBackground = button.terciaryBackgroundTerciaryDisabled,
            disabledText = button.terciaryContentTerciaryDisabled,
            disabledIcon = button.terciaryContentTerciaryDisabled,
            disabledBorder = button.terciaryStrokeTerciaryDisabled
        )
        ButtonType.Destructive -> ButtonColorSpec(
            background = button.destructiveBackgroundDestructiveDefault,
            text = button.destructiveContentDestructiveDefault,
            icon = button.destructiveContentDestructiveDefault,
            border = button.destructiveStrokeDestructiveDefault,
            disabledBackground = button.destructiveBackgroundDestructiveDisabled,
            disabledText = button.destructiveContentDestructiveDisabled,
            disabledIcon = button.destructiveContentDestructiveDisabled,
            disabledBorder = button.destructiveStrokeDestructiveDisabled
        )
        ButtonType.Underlined -> ButtonColorSpec(
            background = transparent,
            text = link.linkPrimaryDefault,
            icon = link.linkPrimaryDefault,
            border = transparent,
            disabledBackground = transparent,
            disabledText = link.linkPrimaryDisabled,
            disabledIcon = link.linkPrimaryDisabled,
            disabledBorder = transparent
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
