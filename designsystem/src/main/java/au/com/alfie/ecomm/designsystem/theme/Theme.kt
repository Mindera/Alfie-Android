package au.com.alfie.ecomm.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import au.com.alfie.ecomm.designsystem.theme.alpha.Alpha
import au.com.alfie.ecomm.designsystem.theme.color.Color
import au.com.alfie.ecomm.designsystem.theme.color.alfieColorScheme
import au.com.alfie.ecomm.designsystem.theme.dimen.FontSize
import au.com.alfie.ecomm.designsystem.theme.dimen.IconSize
import au.com.alfie.ecomm.designsystem.theme.dimen.LineHeight
import au.com.alfie.ecomm.designsystem.theme.dimen.Spacing
import au.com.alfie.ecomm.designsystem.theme.elevation.Elevation
import au.com.alfie.ecomm.designsystem.theme.scale.Scale
import au.com.alfie.ecomm.designsystem.theme.shape.Shape
import au.com.alfie.ecomm.designsystem.theme.shape.alfieShapes
import au.com.alfie.ecomm.designsystem.theme.typography.Typographies
import au.com.alfie.ecomm.designsystem.theme.typography.alfieTypography

@Composable
fun Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = alfieColorScheme(),
        typography = alfieTypography(),
        shapes = alfieShapes(),
        content = content
    )
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
