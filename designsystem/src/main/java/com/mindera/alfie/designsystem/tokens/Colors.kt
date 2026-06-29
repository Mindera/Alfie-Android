// GENERATED — do not edit. Produced by scripts/generate_tokens/generate_design_tokens.py
// Source: designsystem/src/main/assets/design_tokens (Mindera/Alfie-Mobile-Design-Tokens). Re-run on token changes.
@file:Suppress("MagicNumber", "LongMethod")
package com.mindera.alfie.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
interface Colors {
    val surface: ColorSurface
    val content: ColorContent
    val border: ColorBorder
    val button: ColorButton
    val link: ColorLink
}

@Immutable
interface ColorSurface {
    val backgroundDestructive: Color
    val backgroundInvertedPrimary: Color
    val backgroundNegative: Color
    val backgroundPositive: Color
    val backgroundPrimary: Color
    val backgroundPrimaryActive: Color
    val backgroundTerciary: Color
    val foregroundInvertedPrimary: Color
    val foregroundPrimary: Color
}

@Immutable
interface ColorContent {
    val contentInvertedPrimary: Color
    val contentInvertedPrimarydisabled: Color
    val contentNegative: Color
    val contentPositive: Color
    val contentPrimary: Color
    val contentPrimaryActive: Color
    val contentPrimaryDisabled: Color
    val contentTerciary: Color
}

@Immutable
interface ColorBorder {
    val soft: Color
}

@Immutable
interface ColorButton {
    val destructiveBackgroundDestructiveDefault: Color
    val destructiveStrokeDestructiveDefault: Color
    val primaryBackgroundPrimaryDefault: Color
    val secondaryBackgroundSecondaryDefault: Color
    val secondaryBackgroundSecondaryDisabled: Color
    val terciaryBackgroundTerciaryDefault: Color
    val terciaryBackgroundTerciaryDisabled: Color
    val terciaryStrokeTerciaryDefault: Color
    val terciaryStrokeTerciaryDisabled: Color
}

@Immutable
interface ColorLink {
    val linkPrimaryDefault: Color
    val linkPrimaryDisabled: Color
    val linkPrimaryInvertedDefault: Color
    val linkPrimaryInvertedDisabled: Color
}

@Immutable
class DefaultColors(private val primitive: Primitives) : Colors {
    override val surface = object : ColorSurface {
        override val backgroundDestructive = primitive.colors.semanticError600
        override val backgroundInvertedPrimary = primitive.colors.neutrals800
        override val backgroundNegative = primitive.colors.semanticError500
        override val backgroundPositive = primitive.colors.semanticSuccess600
        override val backgroundPrimary = primitive.colors.neutrals0
        override val backgroundPrimaryActive = primitive.colors.neutrals800
        override val backgroundTerciary = primitive.colors.neutrals300
        override val foregroundInvertedPrimary = primitive.colors.neutrals700
        override val foregroundPrimary = primitive.colors.neutrals100
    }
    override val content = object : ColorContent {
        override val contentInvertedPrimary = primitive.colors.neutrals0
        override val contentInvertedPrimarydisabled = primitive.colors.neutrals500
        override val contentNegative = primitive.colors.semanticError500
        override val contentPositive = primitive.colors.semanticSuccess400
        override val contentPrimary = primitive.colors.neutrals800
        override val contentPrimaryActive = primitive.colors.neutrals0
        override val contentPrimaryDisabled = primitive.colors.neutrals400
        override val contentTerciary = primitive.colors.neutrals500
    }
    override val border = object : ColorBorder {
        override val soft = primitive.colors.neutrals200
    }
    override val button = object : ColorButton {
        override val destructiveBackgroundDestructiveDefault = surface.backgroundDestructive
        override val destructiveStrokeDestructiveDefault = surface.backgroundDestructive
        override val primaryBackgroundPrimaryDefault = surface.backgroundInvertedPrimary
        override val secondaryBackgroundSecondaryDefault = primitive.colors.transparent
        override val secondaryBackgroundSecondaryDisabled = primitive.colors.transparent
        override val terciaryBackgroundTerciaryDefault = primitive.colors.transparent
        override val terciaryBackgroundTerciaryDisabled = primitive.colors.transparent
        override val terciaryStrokeTerciaryDefault = primitive.colors.transparent
        override val terciaryStrokeTerciaryDisabled = primitive.colors.transparent
    }
    override val link = object : ColorLink {
        override val linkPrimaryDefault = content.contentPrimary
        override val linkPrimaryDisabled = content.contentPrimaryDisabled
        override val linkPrimaryInvertedDefault = content.contentInvertedPrimary
        override val linkPrimaryInvertedDisabled = content.contentInvertedPrimarydisabled
    }
}
