package com.mindera.alfie.designsystem.component.shadow

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mindera.alfie.designsystem.tokens.LocalTheme

@Composable
fun Modifier.shadow(shadowType: ShadowType, cornersRadius: Dp = 0.dp): Modifier {
    val c = LocalTheme.current.primitive.colors
    val shadowColor = when (shadowType) {
        ShadowType.Elevation1,
        ShadowType.Elevation2,
        ShadowType.Elevation3,
        ShadowType.Elevation4,
        ShadowType.Elevation5 -> c.neutrals900
        else -> c.neutrals800
    }
    return this.elevation(
        color = shadowColor,
        alpha = shadowType.alpha,
        cornersRadius = cornersRadius,
        shadowBlurRadius = shadowType.shadowBlurRadius,
        offsetX = shadowType.offsetX,
        offsetY = shadowType.offsetY
    )
}

fun Modifier.elevation(
    color: Color = Color.Black,
    alpha: Float = 1f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp
): Modifier = drawBehind {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            /* radius = */ shadowBlurRadius.toPx(),
            /* dx = */ offsetX.toPx(),
            /* dy = */ offsetY.toPx(),
            /* shadowColor = */ shadowColor
        )
        it.drawRoundRect(
            left = 0f,
            top = 0f,
            right = this.size.width,
            bottom = this.size.height,
            radiusX = cornersRadius.toPx(),
            radiusY = cornersRadius.toPx(),
            paint = paint
        )
    }
}
