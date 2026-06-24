// GENERATED — do not edit. Produced by scripts/generate_tokens/generate_design_tokens.py
// Source: designsystem/src/main/assets/design_tokens (Mindera/Alfie-Mobile-Design-Tokens). Re-run on token changes.
@file:Suppress("MagicNumber", "LongMethod", "ObjectPropertyNaming")
package com.mindera.alfie.designsystem.tokens

import androidx.compose.ui.graphics.Color

object Colors {
    internal object Primitives {
        object Neutrals {
            val n0 = Color(0xFFFFFFFF)
            val n100 = Color(0xFFF7F7F7)
            val n200 = Color(0xFFE9E9E9)
            val n300 = Color(0xFFCDCDCD)
            val n400 = Color(0xFFA1A1A1)
            val n500 = Color(0xFF767676)
            val n600 = Color(0xFF4A4A4A)
            val n700 = Color(0xFF2B2B2B)
            val n800 = Color(0xFF111111)
            val n900 = Color(0xFF06080A)
        }
        object SemanticError {
            val e100 = Color(0xFFFEF2F1)
            val e200 = Color(0xFFF9DEDC)
            val e300 = Color(0xFFEA9B9E)
            val e400 = Color(0xFFEB676D)
            val e500 = Color(0xFFE03E40)
            val e600 = Color(0xFFB22525)
            val e700 = Color(0xFF952525)
            val e800 = Color(0xFF770500)
        }
        object SemanticSuccess {
            val s100 = Color(0xFFEDF7E7)
            val s200 = Color(0xFFD4EAC3)
            val s300 = Color(0xFFA3CF82)
            val s400 = Color(0xFF84C553)
            val s500 = Color(0xFF60A62B)
            val s600 = Color(0xFF48911F)
            val s700 = Color(0xFF368316)
            val s800 = Color(0xFF006201)
        }
        object Transparent {
            val transparent = Color(0x00FFFFFF)
        }
    }

    object Border {
        val soft = Primitives.Neutrals.n200
    }
    object Button {
        val destructiveBackgroundDestructiveDefault = Primitives.SemanticError.e600
        val destructiveStrokeDestructiveDefault = Primitives.SemanticError.e600
        val primaryBackgroundPrimaryDefault = Primitives.Neutrals.n800
        val secondaryBackgroundSecondaryDefault = Primitives.Transparent.transparent
        val secondaryBackgroundSecondaryDisabled = Primitives.Transparent.transparent
        val terciaryBackgroundTerciaryDefault = Primitives.Transparent.transparent
        val terciaryBackgroundTerciaryDisabled = Primitives.Transparent.transparent
        val terciaryStrokeTerciaryDefault = Primitives.Transparent.transparent
        val terciaryStrokeTerciaryDisabled = Primitives.Transparent.transparent
    }
    object Content {
        val contentInvertedPrimary = Primitives.Neutrals.n0
        val contentInvertedPrimarydisabled = Primitives.Neutrals.n500
        val contentNegative = Primitives.SemanticError.e500
        val contentPositive = Primitives.SemanticSuccess.s400
        val contentPrimary = Primitives.Neutrals.n800
        val contentPrimaryActive = Primitives.Neutrals.n0
        val contentPrimaryDisabled = Primitives.Neutrals.n400
        val contentTerciary = Primitives.Neutrals.n500
    }
    object Link {
        val linkPrimaryDefault = Primitives.Neutrals.n800
        val linkPrimaryDisabled = Primitives.Neutrals.n400
        val linkPrimaryInvertedDefault = Primitives.Neutrals.n0
        val linkPrimaryInvertedDisabled = Primitives.Neutrals.n500
    }
    object Surface {
        val backgroundDestructive = Primitives.SemanticError.e600
        val backgroundInvertedPrimary = Primitives.Neutrals.n800
        val backgroundNegative = Primitives.SemanticError.e500
        val backgroundPositive = Primitives.SemanticSuccess.s600
        val backgroundPrimary = Primitives.Neutrals.n0
        val backgroundPrimaryActive = Primitives.Neutrals.n800
        val backgroundTerciary = Primitives.Neutrals.n300
        val foregroundInvertedPrimary = Primitives.Neutrals.n700
        val foregroundPrimary = Primitives.Neutrals.n100
    }
}
