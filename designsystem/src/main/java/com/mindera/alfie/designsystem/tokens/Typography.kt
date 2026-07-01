// GENERATED — do not edit. Produced by scripts/generate_tokens/generate_design_tokens.py
// Source: designsystem/src/main/assets/design_tokens (Mindera/Alfie-Mobile-Design-Tokens). Re-run on token changes.
@file:Suppress("MagicNumber", "LongMethod")
package com.mindera.alfie.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Immutable
interface Typography {
    val display: TypographyDisplay
    val heading: TypographyHeading
    val body: TypographyBody
    val link: TypographyLink
    val label: TypographyLabel
}

@Immutable
interface TypographyDisplay {
    val large: TextStyle
    val medium: TextStyle
    val small: TextStyle
}

@Immutable
interface TypographyHeading {
    val large: TextStyle
    val medium: TextStyle
    val small: TextStyle
    val xSmall: TextStyle
}

@Immutable
interface TypographyBody {
    val large: TextStyle
    val medium: TextStyle
    val mediumStrikethrough: TextStyle
    val small: TextStyle
}

@Immutable
interface TypographyLink {
    val medium: TextStyle
    val small: TextStyle
}

@Immutable
interface TypographyLabel {
    val small: TextStyle
    val smallBold: TextStyle
}

@Immutable
class DefaultTypography(private val tokens: TypographyTokens) : Typography {
    override val display = object : TypographyDisplay {
        override val large: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.displayLarge.fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = tokens.displayLarge.fontSize,
                lineHeight = tokens.displayLarge.lineHeight,
                letterSpacing = tokens.displayLarge.kerning,
            )
        override val medium: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.displayMedium.fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = tokens.displayMedium.fontSize,
                lineHeight = tokens.displayMedium.lineHeight,
                letterSpacing = tokens.displayMedium.kerning,
            )
        override val small: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.displaySmall.fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = tokens.displaySmall.fontSize,
                lineHeight = tokens.displaySmall.lineHeight,
                letterSpacing = tokens.displaySmall.kerning,
            )
    }
    override val heading = object : TypographyHeading {
        override val large: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.headingLarge.fontFamily,
                fontWeight = FontWeight.W500,
                fontSize = tokens.headingLarge.fontSize,
                lineHeight = tokens.headingLarge.lineHeight,
                letterSpacing = tokens.headingLarge.kerning,
            )
        override val medium: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.headingMedium.fontFamily,
                fontWeight = FontWeight.W500,
                fontSize = tokens.headingMedium.fontSize,
                lineHeight = tokens.headingMedium.lineHeight,
                letterSpacing = tokens.headingMedium.kerning,
            )
        override val small: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.headingSmall.fontFamily,
                fontWeight = FontWeight.W500,
                fontSize = tokens.headingSmall.fontSize,
                lineHeight = tokens.headingSmall.lineHeight,
                letterSpacing = tokens.headingSmall.kerning,
            )
        override val xSmall: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.headingXSmall.fontFamily,
                fontWeight = FontWeight.W500,
                fontSize = tokens.headingXSmall.fontSize,
                lineHeight = tokens.headingXSmall.lineHeight,
                letterSpacing = tokens.headingXSmall.kerning,
            )
    }
    override val body = object : TypographyBody {
        override val large: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.bodyLarge.fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = tokens.bodyLarge.fontSize,
                lineHeight = tokens.bodyLarge.lineHeight,
                letterSpacing = tokens.bodyLarge.kerning,
            )
        override val medium: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.bodyMedium.fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = tokens.bodyMedium.fontSize,
                lineHeight = tokens.bodyMedium.lineHeight,
                letterSpacing = tokens.bodyMedium.kerning,
            )
        override val mediumStrikethrough: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.bodyMediumStrikethrough.fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = tokens.bodyMediumStrikethrough.fontSize,
                lineHeight = tokens.bodyMediumStrikethrough.lineHeight,
                letterSpacing = tokens.bodyMediumStrikethrough.kerning,
            )
        override val small: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.bodySmall.fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = tokens.bodySmall.fontSize,
                lineHeight = tokens.bodySmall.lineHeight,
                letterSpacing = tokens.bodySmall.kerning,
            )
    }
    override val link = object : TypographyLink {
        override val medium: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.linkMedium.fontFamily,
                fontWeight = FontWeight.W500,
                fontSize = tokens.linkMedium.fontSize,
                lineHeight = tokens.linkMedium.lineHeight,
                letterSpacing = tokens.linkMedium.kerning,
            )
        override val small: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.linkSmall.fontFamily,
                fontWeight = FontWeight.W500,
                fontSize = tokens.linkSmall.fontSize,
                lineHeight = tokens.linkSmall.lineHeight,
                letterSpacing = tokens.linkSmall.kerning,
            )
    }
    override val label = object : TypographyLabel {
        override val small: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.labelSmall.fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = tokens.labelSmall.fontSize,
                lineHeight = tokens.labelSmall.lineHeight,
                letterSpacing = tokens.labelSmall.kerning,
            )
        override val smallBold: TextStyle =
            TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = tokens.labelSmallBold.fontFamily,
                fontWeight = FontWeight.W500,
                fontSize = tokens.labelSmallBold.fontSize,
                lineHeight = tokens.labelSmallBold.lineHeight,
                letterSpacing = tokens.labelSmallBold.kerning,
            )
    }
}
