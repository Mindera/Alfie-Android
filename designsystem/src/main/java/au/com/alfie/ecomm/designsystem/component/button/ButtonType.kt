package au.com.alfie.ecomm.designsystem.component.button

import androidx.compose.ui.graphics.Color
import au.com.alfie.ecomm.designsystem.component.loading.LoadingType
import au.com.alfie.ecomm.designsystem.component.loading.LoadingType.Dark
import au.com.alfie.ecomm.designsystem.component.loading.LoadingType.Light
import au.com.alfie.ecomm.designsystem.component.shimmer.ShimmerColors
import au.com.alfie.ecomm.designsystem.theme.Theme

enum class ButtonType(
    val backgroundColor: Color,
    val contentColor: Color,
    val disabledBackgroundColor: Color,
    val disabledContentColor: Color,
    val hasBorder: Boolean,
    val loadingType: LoadingType,
    val disabledLoadingType: LoadingType,
    val shimmerColors: ShimmerColors = ShimmerColors.Light
) {
    Primary(
        backgroundColor = Theme.color.primary.mono900,
        contentColor = Theme.color.white,
        disabledBackgroundColor = Theme.color.primary.mono050,
        disabledContentColor = Theme.color.primary.mono300,
        hasBorder = false,
        loadingType = Light,
        disabledLoadingType = Dark,
        shimmerColors = ShimmerColors.Dark
    ),
    Secondary(
        backgroundColor = Theme.color.white,
        contentColor = Theme.color.primary.mono900,
        disabledBackgroundColor = Theme.color.white,
        disabledContentColor = Theme.color.primary.mono300,
        hasBorder = true,
        loadingType = Dark,
        disabledLoadingType = Dark
    ),
    Tertiary(
        backgroundColor = Theme.color.white,
        contentColor = Theme.color.primary.mono900,
        disabledBackgroundColor = Theme.color.white,
        disabledContentColor = Theme.color.primary.mono300,
        hasBorder = false,
        loadingType = Dark,
        disabledLoadingType = Dark
    ),
    Underlined(
        backgroundColor = Theme.color.white,
        contentColor = Theme.color.primary.mono900,
        disabledBackgroundColor = Theme.color.white,
        disabledContentColor = Theme.color.primary.mono300,
        hasBorder = false,
        loadingType = Dark,
        disabledLoadingType = Dark
    )
}
