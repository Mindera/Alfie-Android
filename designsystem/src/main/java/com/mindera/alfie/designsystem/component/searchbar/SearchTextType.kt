package com.mindera.alfie.designsystem.component.searchbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mindera.alfie.designsystem.icons.AlfieIcons
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
    @DrawableRes val clearIcon: Int
) {
    Light(
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close
    ),
    Dark(
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close
    ),
    Soft(
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close
    ),
    SoftLarge(
        searchIcon = AlfieIcons.Search,
        clearIcon = AlfieIcons.Close
    )
}

/**
 * Field geometry per variant, read from the new token system. Only `Soft` is used in
 * production (search field spec: 4dp radius, start 8 / end 16 padding, 8dp text↔icon
 * gap); the other three exist for the debug catalog and keep their legacy pill look.
 *
 * [hasClearButton] follows the Figma "Search (Active)" field (node `I673:87056;1:11346`):
 * once the clear button appears it owns the end inset — a 40dp tertiary button flush with the
 * border — so the field drops its own end and vertical padding. The field is 40dp tall either
 * way (8 + 24 + 8 without the button, the button's own 40 with it).
 */
@Composable
fun SearchTextType.contentPadding(hasClearButton: Boolean = false): PaddingValues {
    val spacing = LocalTheme.current.spacing
    if (this == SearchTextType.Soft && hasClearButton) {
        return PaddingValues(start = spacing.spacing8)
    }
    return when (this) {
        SearchTextType.Light -> PaddingValues(
            start = spacing.spacing12,
            top = spacing.spacing8,
            end = spacing.spacing12,
            bottom = spacing.spacing8
        )
        // Dark's legacy 10dp vertical rhythm has no primitive step — catalog-only literal.
        SearchTextType.Dark -> PaddingValues(
            start = spacing.spacing12,
            top = 10.dp,
            end = spacing.spacing12,
            bottom = 10.dp
        )
        SearchTextType.Soft -> PaddingValues(
            start = spacing.spacing8,
            top = spacing.spacing8,
            end = spacing.spacing16,
            bottom = spacing.spacing8
        )
        SearchTextType.SoftLarge -> PaddingValues(
            start = spacing.spacing12,
            top = spacing.spacing14,
            end = spacing.spacing12,
            bottom = spacing.spacing14
        )
    }
}

@Composable
fun SearchTextType.shape(): Shape = when (this) {
    // Search field spec: border-radius/default (4dp)
    SearchTextType.Soft -> LocalTheme.current.sizing.radius.soft
    // NewTheme exposes no pill radius; rounded is CircleShape, the legacy pill.
    else -> LocalTheme.current.sizing.radius.rounded
}

@Composable
fun SearchTextType.iconGap(): Dp = when (this) {
    SearchTextType.Soft -> LocalTheme.current.spacing.spacing8
    // 6dp has no primitive step — catalog-only literal.
    else -> 6.dp
}

/**
 * Tap target of the trailing clear affordance. `Soft` matches the Figma tertiary button that
 * hosts the glyph (40dp box around a 24dp icon); the catalog variants keep the bare icon.
 */
@Composable
fun SearchTextType.clearButtonSize(): Dp = when (this) {
    SearchTextType.Soft -> LocalTheme.current.sizing.icon.xlarge
    else -> LocalTheme.current.sizing.icon.medium
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
        // Idle is the landing-screen entry point (Figma "Search", node I671:78533;17:15275): a
        // flat foreground-primary fill with no stroke. Focusing it turns the field into the white,
        // border/soft-outlined input of the Search page (node I673:87055;1:11326).
        SearchTextType.Soft -> SearchTextColorSpec(
            textColor = semantic.content.contentPrimary,
            cursorColor = semantic.content.contentPrimary,
            unselectedBorderColor = primitive.transparent,
            selectedBorderColor = semantic.border.soft,
            placeholderTextColor = semantic.content.contentTerciary,
            selectedColor = semantic.surface.backgroundPrimary,
            unselectedColor = semantic.surface.foregroundPrimary
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
