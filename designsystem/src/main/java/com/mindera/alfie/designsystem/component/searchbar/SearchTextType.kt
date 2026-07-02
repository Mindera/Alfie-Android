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
    val textStyle: TextStyle,
    @DrawableRes val searchIcon: Int,
    @DrawableRes val clearIcon: Int,
    val verticalPadding: Dp,
    val horizontalPadding: Dp
) {
    Light(
        textStyle = Theme.typography.paragraph,
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close,
        verticalPadding = Theme.spacing.spacing8,
        horizontalPadding = Theme.spacing.spacing12
    ),
    Dark(
        textStyle = Theme.typography.paragraph,
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close,
        verticalPadding = Theme.spacing.spacing10,
        horizontalPadding = Theme.spacing.spacing12
    ),
    Soft(
        textStyle = Theme.typography.paragraph,
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close,
        verticalPadding = Theme.spacing.spacing8,
        horizontalPadding = Theme.spacing.spacing12
    ),
    SoftLarge(
        textStyle = Theme.typography.paragraph,
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close,
        verticalPadding = Theme.spacing.spacing14,
        horizontalPadding = Theme.spacing.spacing12
    )
}

@Composable
fun SearchTextType.colorSpec(): SearchTextColorSpec {
    val c = LocalTheme.current.primitive.colors
    return when (this) {
        SearchTextType.Light -> SearchTextColorSpec(
            textColor = c.neutrals800,
            cursorColor = c.neutrals900,
            unselectedBorderColor = c.neutrals800,
            selectedBorderColor = c.neutrals800,
            placeholderTextColor = c.neutrals500,
            selectedColor = c.neutrals100,
            unselectedColor = c.neutrals0
        )
        SearchTextType.Dark -> SearchTextColorSpec(
            textColor = c.neutrals300,
            cursorColor = c.neutrals200,
            unselectedBorderColor = c.transparent,
            selectedBorderColor = c.neutrals300,
            placeholderTextColor = c.neutrals200,
            selectedColor = c.neutrals700,
            unselectedColor = c.neutrals700
        )
        SearchTextType.Soft -> SearchTextColorSpec(
            textColor = c.neutrals800,
            cursorColor = c.neutrals900,
            unselectedBorderColor = c.transparent,
            selectedBorderColor = c.neutrals200,
            placeholderTextColor = c.neutrals500,
            selectedColor = c.neutrals100,
            unselectedColor = c.neutrals100
        )
        SearchTextType.SoftLarge -> SearchTextColorSpec(
            textColor = c.neutrals500,
            cursorColor = c.neutrals900,
            unselectedBorderColor = c.transparent,
            selectedBorderColor = c.neutrals200,
            placeholderTextColor = c.neutrals500,
            selectedColor = c.neutrals100,
            unselectedColor = c.neutrals100
        )
    }
}
