// TEMP bridge until body-medium-bold is exported to the token JSON + regenerated (separate PR).
// Figma: body/medium-bold — SF Pro Medium 16/24, kerning 0 (Android: Roboto Medium).
package com.mindera.alfie.designsystem.tokens

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

val TypographyBody.mediumBold: TextStyle
    get() = medium.copy(fontWeight = FontWeight.Medium)
