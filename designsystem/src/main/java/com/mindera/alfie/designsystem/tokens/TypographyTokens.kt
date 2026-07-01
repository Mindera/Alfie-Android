// GENERATED — do not edit. Produced by scripts/generate_tokens/generate_design_tokens.py
// Source: designsystem/src/main/assets/design_tokens (Mindera/Alfie-Mobile-Design-Tokens). Re-run on token changes.
@file:Suppress("MagicNumber", "LongMethod")
package com.mindera.alfie.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit

@Immutable
interface StyleTokens {
    val fontFamily: FontFamily
    val fontSize: TextUnit
    val lineHeight: TextUnit
    val kerning: TextUnit
}

@Immutable
interface TypographyTokens {
    val displayLarge: StyleTokens
    val displayMedium: StyleTokens
    val displaySmall: StyleTokens
    val headingLarge: StyleTokens
    val headingMedium: StyleTokens
    val headingSmall: StyleTokens
    val headingXSmall: StyleTokens
    val bodyLarge: StyleTokens
    val bodyMedium: StyleTokens
    val bodySmall: StyleTokens
    val labelSmall: StyleTokens
    val bodyMediumStrikethrough: StyleTokens
    val linkMedium: StyleTokens
    val linkSmall: StyleTokens
    val labelSmallBold: StyleTokens
}

@Immutable
class DefaultTypographyTokens(private val primitive: Primitives) : TypographyTokens {
    override val displayLarge = object : StyleTokens {
        override val fontFamily = primitive.typography.fontFamily.brand
        override val fontSize = primitive.typography.fontSize.fontSize24
        override val lineHeight = primitive.typography.lineHeight.lineHeight40
        override val kerning = primitive.typography.kerning.none
    }
    override val displayMedium = object : StyleTokens {
        override val fontFamily = primitive.typography.fontFamily.brand
        override val fontSize = primitive.typography.fontSize.fontSize20
        override val lineHeight = primitive.typography.lineHeight.lineHeight32
        override val kerning = primitive.typography.kerning.none
    }
    override val displaySmall = object : StyleTokens {
        override val fontFamily = primitive.typography.fontFamily.brand
        override val fontSize = primitive.typography.fontSize.fontSize18
        override val lineHeight = primitive.typography.lineHeight.lineHeight28
        override val kerning = primitive.typography.kerning.none
    }
    override val headingLarge = object : StyleTokens {
        override val fontFamily = primitive.typography.fontFamily.primaryAndroid
        override val fontSize = primitive.typography.fontSize.fontSize32
        override val lineHeight = primitive.typography.lineHeight.lineHeight40
        override val kerning = primitive.typography.kerning.tight
    }
    override val headingMedium = object : StyleTokens {
        override val fontFamily = primitive.typography.fontFamily.primaryAndroid
        override val fontSize = primitive.typography.fontSize.fontSize24
        override val lineHeight = primitive.typography.lineHeight.lineHeight32
        override val kerning = primitive.typography.kerning.tight
    }
    override val headingSmall = object : StyleTokens {
        override val fontFamily = primitive.typography.fontFamily.primaryAndroid
        override val fontSize = primitive.typography.fontSize.fontSize20
        override val lineHeight = primitive.typography.lineHeight.lineHeight28
        override val kerning = primitive.typography.kerning.tight
    }
    override val headingXSmall = object : StyleTokens {
        override val fontFamily = primitive.typography.fontFamily.primaryAndroid
        override val fontSize = primitive.typography.fontSize.fontSize16
        override val lineHeight = primitive.typography.lineHeight.lineHeight20
        override val kerning = primitive.typography.kerning.tight
    }
    override val bodyLarge = object : StyleTokens {
        override val fontFamily = primitive.typography.fontFamily.primaryAndroid
        override val fontSize = primitive.typography.fontSize.fontSize18
        override val lineHeight = primitive.typography.lineHeight.lineHeight28
        override val kerning = primitive.typography.kerning.none
    }
    override val bodyMedium = object : StyleTokens {
        override val fontFamily = primitive.typography.fontFamily.primaryAndroid
        override val fontSize = primitive.typography.fontSize.fontSize16
        override val lineHeight = primitive.typography.lineHeight.lineHeight24
        override val kerning = primitive.typography.kerning.none
    }
    override val bodySmall = object : StyleTokens {
        override val fontFamily = primitive.typography.fontFamily.primaryAndroid
        override val fontSize = primitive.typography.fontSize.fontSize12
        override val lineHeight = primitive.typography.lineHeight.lineHeight16
        override val kerning = primitive.typography.kerning.none
    }
    override val labelSmall = object : StyleTokens {
        override val fontFamily = primitive.typography.fontFamily.primaryAndroid
        override val fontSize = primitive.typography.fontSize.fontSize12
        override val lineHeight = primitive.typography.lineHeight.lineHeight16
        override val kerning = primitive.typography.kerning.none
    }
    override val bodyMediumStrikethrough = object : StyleTokens {
        override val fontFamily = bodyMedium.fontFamily
        override val fontSize = bodyMedium.fontSize
        override val lineHeight = bodyMedium.lineHeight
        override val kerning = primitive.typography.kerning.none
    }
    override val linkMedium = object : StyleTokens {
        override val fontFamily = bodyMedium.fontFamily
        override val fontSize = bodyMedium.fontSize
        override val lineHeight = bodyMedium.lineHeight
        override val kerning = bodyMedium.kerning
    }
    override val linkSmall = object : StyleTokens {
        override val fontFamily = bodySmall.fontFamily
        override val fontSize = bodySmall.fontSize
        override val lineHeight = bodySmall.lineHeight
        override val kerning = bodySmall.kerning
    }
    override val labelSmallBold = object : StyleTokens {
        override val fontFamily = labelSmall.fontFamily
        override val fontSize = labelSmall.fontSize
        override val lineHeight = labelSmall.lineHeight
        override val kerning = labelSmall.kerning
    }
}
