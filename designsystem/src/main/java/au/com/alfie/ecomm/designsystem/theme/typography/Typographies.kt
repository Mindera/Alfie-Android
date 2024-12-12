package au.com.alfie.ecomm.designsystem.theme.typography

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.sp
import au.com.alfie.ecomm.designsystem.theme.Theme

object Typographies {

    val heading1: TextStyle = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontFamily = FreightDisplay,
        fontWeight = Normal,
        fontSize = Theme.fontSize.xxLarge,
        lineHeight = Theme.lineHeight.xxLarge
    )

    val heading2: TextStyle = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontFamily = CircularBook,
        fontWeight = Bold,
        fontSize = Theme.fontSize.xLarge,
        lineHeight = Theme.lineHeight.xLarge
    )

    val heading3: TextStyle = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontFamily = CircularSemiBold,
        fontWeight = Medium,
        fontSize = Theme.fontSize.large,
        lineHeight = Theme.lineHeight.large
    )

    val paragraph: TextStyle = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontFamily = CircularBook,
        fontWeight = Normal,
        fontSize = Theme.fontSize.medium,
        lineHeight = Theme.lineHeight.medium
    )

    val paragraphLarge: TextStyle = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontFamily = CircularBook,
        fontWeight = Normal,
        fontSize = 18.sp,
        lineHeight = Theme.lineHeight.large
    )

    val paragraphXLarge: TextStyle = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontFamily = CircularBook,
        fontWeight = Normal,
        fontSize = Theme.fontSize.medium,
        lineHeight = Theme.lineHeight.large
    )

    val paragraphItalic: TextStyle = paragraph.copy(fontFamily = CircularBookItalic)

    val paragraphUnderlined: TextStyle = paragraph.copy(textDecoration = Underline)

    val paragraphStrikethrough: TextStyle = paragraph.copy(textDecoration = LineThrough)

    val paragraphBold: TextStyle = paragraph.copy(fontWeight = Bold)

    val paragraphBoldItalic: TextStyle = paragraphBold.copy(fontFamily = CircularBookItalic)

    val paragraphBoldUnderline: TextStyle = paragraphBold.copy(textDecoration = Underline)

    val paragraphBoldStrikethrough: TextStyle = paragraphBold.copy(textDecoration = LineThrough)

    val small: TextStyle = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontFamily = CircularBook,
        fontWeight = Normal,
        fontSize = Theme.fontSize.small,
        lineHeight = Theme.lineHeight.small
    )

    val smallItalic: TextStyle = small.copy(fontFamily = CircularBookItalic)

    val smallUnderlined: TextStyle = small.copy(textDecoration = Underline)

    val smallStrikethrough: TextStyle = small.copy(textDecoration = LineThrough)

    val smallBold: TextStyle = small.copy(fontWeight = Bold)

    val smallBoldItalic: TextStyle = smallBold.copy(fontFamily = CircularBookItalic)

    val smallBoldUnderline: TextStyle = smallBold.copy(textDecoration = Underline)

    val smallBoldStrikethrough: TextStyle = smallBold.copy(textDecoration = LineThrough)

    val tiny: TextStyle = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontFamily = CircularBook,
        fontWeight = Normal,
        fontSize = Theme.fontSize.tiny,
        lineHeight = Theme.lineHeight.small
    )

    val tinyItalic: TextStyle = tiny.copy(fontFamily = CircularBookItalic)

    val tinyStrikethrough: TextStyle = tiny.copy(textDecoration = LineThrough)

    val tinyBold: TextStyle = tiny.copy(fontWeight = Bold)

    val tinyBoldItalic: TextStyle = tinyBold.copy(fontFamily = CircularBookItalic)

    val tinyBoldUnderline: TextStyle = tinyBold.copy(textDecoration = Underline)
}
