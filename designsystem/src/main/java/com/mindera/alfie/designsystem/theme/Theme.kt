package com.mindera.alfie.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.mindera.alfie.designsystem.theme.alpha.Alpha
import com.mindera.alfie.designsystem.theme.color.Color
import com.mindera.alfie.designsystem.theme.dimen.FontSize
import com.mindera.alfie.designsystem.theme.dimen.IconSize
import com.mindera.alfie.designsystem.theme.dimen.LineHeight
import com.mindera.alfie.designsystem.theme.dimen.Spacing
import com.mindera.alfie.designsystem.theme.elevation.Elevation
import com.mindera.alfie.designsystem.theme.scale.Scale
import com.mindera.alfie.designsystem.theme.shape.Shape
import com.mindera.alfie.designsystem.theme.typography.Typographies
import com.mindera.alfie.designsystem.tokens.ProvideNewTheme

private val alfieColorScheme = lightColorScheme(
    background = Theme.color.white
)

@Composable
fun Theme(content: @Composable () -> Unit) {
    ProvideNewTheme {
        MaterialTheme(
            colorScheme = alfieColorScheme,
            content = content
        )
    }
}

object Theme {

    val alpha: Alpha = Alpha

    val color: Color = Color

    val elevation: Elevation = Elevation

    val fontSize: FontSize = FontSize

    val iconSize: IconSize = IconSize

    val lineHeight: LineHeight = LineHeight

    val scale: Scale = Scale

    val shape: Shape = Shape

    val spacing: Spacing = Spacing

    val typography: Typographies = Typographies
}
