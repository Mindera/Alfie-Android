// GENERATED — do not edit. Produced by scripts/generate_tokens/generate_design_tokens.py
// Source: designsystem/src/main/assets/design_tokens (Mindera/Alfie-Mobile-Design-Tokens). Re-run on token changes.
@file:Suppress("MagicNumber", "LongMethod")
package com.mindera.alfie.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindera.alfie.designsystem.R

@Immutable
interface Primitives {
    val colors: PrimitiveColors
    val spacing: PrimitiveSpacing
    val typography: PrimitiveTypography
    val border: PrimitiveBorder
}

@Immutable
interface PrimitiveColors {
    val neutrals0: Color
    val neutrals100: Color
    val neutrals200: Color
    val neutrals300: Color
    val neutrals400: Color
    val neutrals500: Color
    val neutrals600: Color
    val neutrals700: Color
    val neutrals800: Color
    val neutrals900: Color
    val semanticError100: Color
    val semanticError200: Color
    val semanticError300: Color
    val semanticError400: Color
    val semanticError500: Color
    val semanticError600: Color
    val semanticError700: Color
    val semanticError800: Color
    val semanticSuccess100: Color
    val semanticSuccess200: Color
    val semanticSuccess300: Color
    val semanticSuccess400: Color
    val semanticSuccess500: Color
    val semanticSuccess600: Color
    val semanticSuccess700: Color
    val semanticSuccess800: Color
    val transparent: Color
}

@Immutable
interface PrimitiveSpacing {
    val spacing0: Dp
    val spacing2: Dp
    val spacing4: Dp
    val spacing8: Dp
    val spacing12: Dp
    val spacing14: Dp
    val spacing16: Dp
    val spacing18: Dp
    val spacing20: Dp
    val spacing24: Dp
    val spacing28: Dp
    val spacing32: Dp
    val spacing40: Dp
    val spacing48: Dp
    val spacing56: Dp
    val spacing64: Dp
    val spacing80: Dp
    val spacing96: Dp
    val spacing124: Dp
    val spacing220: Dp
}

@Immutable
interface PrimitiveFontFamilies {
    val brand: FontFamily
    val primaryAndroid: FontFamily
    val primaryIos: FontFamily
    val primaryWeb: FontFamily
}

@Immutable
interface PrimitiveFontSizes {
    val fontSize12: TextUnit
    val fontSize14: TextUnit
    val fontSize16: TextUnit
    val fontSize18: TextUnit
    val fontSize20: TextUnit
    val fontSize24: TextUnit
    val fontSize32: TextUnit
    val fontSize40: TextUnit
    val fontSize48: TextUnit
    val fontSize56: TextUnit
}

@Immutable
interface PrimitiveLineHeights {
    val lineHeight16: TextUnit
    val lineHeight20: TextUnit
    val lineHeight24: TextUnit
    val lineHeight28: TextUnit
    val lineHeight32: TextUnit
    val lineHeight40: TextUnit
    val lineHeight48: TextUnit
}

@Immutable
interface PrimitiveKernings {
    val none: TextUnit
    val spacious: TextUnit
    val tight: TextUnit
}

@Immutable
interface PrimitiveTypography {
    val fontFamily: PrimitiveFontFamilies
    val fontSize: PrimitiveFontSizes
    val lineHeight: PrimitiveLineHeights
    val kerning: PrimitiveKernings
}

@Immutable
interface PrimitiveBorder {
    val weightDefault: Dp
}

@Immutable
object LightPrimitives : Primitives {
    override val colors = object : PrimitiveColors {
        override val neutrals0 = Color(0xFFFFFFFF)
        override val neutrals100 = Color(0xFFF7F7F7)
        override val neutrals200 = Color(0xFFE9E9E9)
        override val neutrals300 = Color(0xFFCDCDCD)
        override val neutrals400 = Color(0xFFA1A1A1)
        override val neutrals500 = Color(0xFF767676)
        override val neutrals600 = Color(0xFF4A4A4A)
        override val neutrals700 = Color(0xFF2B2B2B)
        override val neutrals800 = Color(0xFF111111)
        override val neutrals900 = Color(0xFF06080A)
        override val semanticError100 = Color(0xFFFEF2F1)
        override val semanticError200 = Color(0xFFF9DEDC)
        override val semanticError300 = Color(0xFFEA9B9E)
        override val semanticError400 = Color(0xFFEB676D)
        override val semanticError500 = Color(0xFFE03E40)
        override val semanticError600 = Color(0xFFB22525)
        override val semanticError700 = Color(0xFF952525)
        override val semanticError800 = Color(0xFF770500)
        override val semanticSuccess100 = Color(0xFFEDF7E7)
        override val semanticSuccess200 = Color(0xFFD4EAC3)
        override val semanticSuccess300 = Color(0xFFA3CF82)
        override val semanticSuccess400 = Color(0xFF84C553)
        override val semanticSuccess500 = Color(0xFF60A62B)
        override val semanticSuccess600 = Color(0xFF48911F)
        override val semanticSuccess700 = Color(0xFF368316)
        override val semanticSuccess800 = Color(0xFF006201)
        override val transparent = Color(0x00FFFFFF)
    }
    override val spacing = object : PrimitiveSpacing {
        override val spacing0 = 0.dp
        override val spacing2 = 2.dp
        override val spacing4 = 4.dp
        override val spacing8 = 8.dp
        override val spacing12 = 12.dp
        override val spacing14 = 14.dp
        override val spacing16 = 16.dp
        override val spacing18 = 18.dp
        override val spacing20 = 20.dp
        override val spacing24 = 24.dp
        override val spacing28 = 28.dp
        override val spacing32 = 32.dp
        override val spacing40 = 40.dp
        override val spacing48 = 48.dp
        override val spacing56 = 56.dp
        override val spacing64 = 64.dp
        override val spacing80 = 80.dp
        override val spacing96 = 96.dp
        override val spacing124 = 124.dp
        override val spacing220 = 220.dp
    }
    override val typography = object : PrimitiveTypography {
        override val fontFamily = object : PrimitiveFontFamilies {
            override val brand: FontFamily = FontFamily(
                Font(R.font.libre_baskerville_regular, FontWeight.Normal),
                Font(R.font.libre_baskerville_medium, FontWeight.W500),
                Font(R.font.libre_baskerville_semibold, FontWeight.W600),
                Font(R.font.libre_baskerville_bold, FontWeight.Bold),
            )
            override val primaryAndroid: FontFamily = FontFamily(
                Font(R.font.roboto_regular, FontWeight.Normal),
                Font(R.font.roboto_medium, FontWeight.W500),
                Font(R.font.roboto_semibold, FontWeight.W600),
                Font(R.font.roboto_bold, FontWeight.Bold),
            )
            override val primaryIos: FontFamily = FontFamily.Default // not bundled on Android profile
            override val primaryWeb: FontFamily = FontFamily.Default // not bundled on Android profile
        }
        override val fontSize = object : PrimitiveFontSizes {
            override val fontSize12 = 12.sp
            override val fontSize14 = 14.sp
            override val fontSize16 = 16.sp
            override val fontSize18 = 18.sp
            override val fontSize20 = 20.sp
            override val fontSize24 = 24.sp
            override val fontSize32 = 32.sp
            override val fontSize40 = 40.sp
            override val fontSize48 = 48.sp
            override val fontSize56 = 56.sp
        }
        override val lineHeight = object : PrimitiveLineHeights {
            override val lineHeight16 = 16.sp
            override val lineHeight20 = 20.sp
            override val lineHeight24 = 24.sp
            override val lineHeight28 = 28.sp
            override val lineHeight32 = 32.sp
            override val lineHeight40 = 40.sp
            override val lineHeight48 = 48.sp
        }
        override val kerning = object : PrimitiveKernings {
            override val none = 0.sp
            override val spacious = 1.sp
            override val tight = (-0.5).sp
        }
    }
    override val border = object : PrimitiveBorder {
        override val weightDefault = 1.dp
    }
}
