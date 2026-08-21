package com.mindera.alfie.designsystem.component.searchbar

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

@Immutable
data class SearchTextColorSpec(
    val textColor: Color,
    val cursorColor: Color,
    val unselectedBorderColor: Color,
    val selectedBorderColor: Color,
    val placeholderTextColor: Color,
    val selectedColor: Color,
    val unselectedColor: Color
)

enum class SearchTextType(
    @DrawableRes val searchIcon: Int,
    @DrawableRes val clearIcon: Int,
    val verticalPadding: Dp,
    val startPadding: Dp,
    val endPadding: Dp
) {
    Light(
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close,
        verticalPadding = Theme.spacing.spacing8,
        startPadding = Theme.spacing.spacing12,
        endPadding = Theme.spacing.spacing12
    ),
    Dark(
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close,
        verticalPadding = Theme.spacing.spacing10,
        startPadding = Theme.spacing.spacing12,
        endPadding = Theme.spacing.spacing12
    ),
    Soft(
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close,
        verticalPadding = Theme.spacing.spacing8,
        startPadding = Theme.spacing.spacing8,
        endPadding = Theme.spacing.spacing16
    ),
    SoftLarge(
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close,
        verticalPadding = Theme.spacing.spacing14,
        startPadding = Theme.spacing.spacing12,
        endPadding = Theme.spacing.spacing12
    )
}

/** Composable accessor — all search types currently use body.medium. */
val SearchTextType.textStyle: TextStyle
    @Composable get() = LocalTheme.current.typography.body.medium

@Composable
fun SearchTextType.colorSpec(): SearchTextColorSpec {
    val primitive = LocalTheme.current.primitive.colors
    val semantic = LocalTheme.current.color
    return when (this) {
        SearchTextType.Light -> SearchTextColorSpec(
            textColor = primitive.neutrals800,
            cursorColor = primitive.neutrals900,
            unselectedBorderColor = primitive.neutrals800,
            selectedBorderColor = primitive.neutrals800,
            placeholderTextColor = primitive.neutrals500,
            selectedColor = primitive.neutrals100,
            unselectedColor = primitive.neutrals0
        )
        SearchTextType.Dark -> SearchTextColorSpec(
            textColor = primitive.neutrals300,
            cursorColor = primitive.neutrals200,
            unselectedBorderColor = primitive.transparent,
            selectedBorderColor = primitive.neutrals300,
            placeholderTextColor = primitive.neutrals200,
            selectedColor = primitive.neutrals700,
            unselectedColor = primitive.neutrals700
        )
        SearchTextType.Soft -> SearchTextColorSpec(
            textColor = semantic.content.contentPrimary,
            cursorColor = primitive.neutrals900,
            unselectedBorderColor = semantic.border.soft,
            selectedBorderColor = semantic.border.soft,
            placeholderTextColor = semantic.content.contentTerciary,
            selectedColor = semantic.surface.backgroundPrimary,
            unselectedColor = semantic.surface.backgroundPrimary
        )
        SearchTextType.SoftLarge -> SearchTextColorSpec(
            textColor = primitive.neutrals500,
            cursorColor = primitive.neutrals900,
            unselectedBorderColor = primitive.transparent,
            selectedBorderColor = primitive.neutrals200,
            placeholderTextColor = primitive.neutrals500,
            selectedColor = primitive.neutrals100,
            unselectedColor = primitive.neutrals100
        )
    }
}
