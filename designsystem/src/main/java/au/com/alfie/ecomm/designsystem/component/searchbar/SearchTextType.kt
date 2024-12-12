package au.com.alfie.ecomm.designsystem.component.searchbar

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.designsystem.R as RD

enum class SearchTextType(
    val textStyle: TextStyle,
    val textColor: Color,
    @DrawableRes val searchIcon: Int,
    @DrawableRes val clearIcon: Int,
    val cursorColor: Color,
    val unselectedBorderColor: Color,
    val selectedBorderColor: Color,
    val placeholderTextColor: Color,
    val selectedColor: Color,
    val unselectedColor: Color,
    val verticalPadding: Dp,
    val horizontalPadding: Dp
) {
    Light(
        textStyle = Theme.typography.paragraph,
        textColor = Theme.color.primary.mono900,
        searchIcon = RD.drawable.ic_action_search_dark,
        clearIcon = RD.drawable.ic_action_close_dark,
        cursorColor = Theme.color.black,
        unselectedBorderColor = Theme.color.primary.mono900,
        selectedBorderColor = Theme.color.primary.mono900,
        placeholderTextColor = Theme.color.primary.mono500,
        selectedColor = Theme.color.primary.mono050,
        unselectedColor = Theme.color.white,
        verticalPadding = Theme.spacing.spacing8,
        horizontalPadding = Theme.spacing.spacing12
    ),
    Dark(
        textStyle = Theme.typography.paragraph,
        textColor = Theme.color.primary.mono300,
        searchIcon = RD.drawable.ic_action_search_light,
        clearIcon = RD.drawable.ic_action_close_light,
        cursorColor = Theme.color.primary.mono200,
        unselectedBorderColor = Color.Transparent,
        selectedBorderColor = Theme.color.primary.mono300,
        placeholderTextColor = Theme.color.primary.mono200,
        selectedColor = Theme.color.primary.mono800,
        unselectedColor = Theme.color.primary.mono800,
        verticalPadding = Theme.spacing.spacing10,
        horizontalPadding = Theme.spacing.spacing12
    ),
    Soft(
        textStyle = Theme.typography.paragraph,
        textColor = Theme.color.primary.mono900,
        searchIcon = RD.drawable.ic_action_search_dark,
        clearIcon = RD.drawable.ic_action_close_dark,
        cursorColor = Theme.color.black,
        unselectedBorderColor = Color.Transparent,
        selectedBorderColor = Theme.color.primary.mono200,
        placeholderTextColor = Theme.color.primary.mono500,
        selectedColor = Theme.color.primary.mono050,
        unselectedColor = Theme.color.primary.mono050,
        verticalPadding = Theme.spacing.spacing8,
        horizontalPadding = Theme.spacing.spacing12
    ),
    SoftLarge(
        textStyle = Theme.typography.paragraph,
        textColor = Theme.color.primary.mono500,
        searchIcon = RD.drawable.ic_action_search_dark,
        clearIcon = RD.drawable.ic_action_close_dark,
        cursorColor = Theme.color.black,
        unselectedBorderColor = Color.Transparent,
        selectedBorderColor = Theme.color.primary.mono200,
        placeholderTextColor = Theme.color.primary.mono500,
        selectedColor = Theme.color.primary.mono050,
        unselectedColor = Theme.color.primary.mono050,
        verticalPadding = Theme.spacing.spacing14,
        horizontalPadding = Theme.spacing.spacing12
    )
}
