package au.com.alfie.ecomm.designsystem.component.shadow

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.theme.Theme

enum class ShadowType(
    val color: Color,
    val alpha: Float,
    val shadowBlurRadius: Dp,
    val offsetX: Dp,
    val offsetY: Dp
) {
    Elevation1(
        color = Theme.color.black,
        alpha = 0.15f,
        shadowBlurRadius = 1.dp,
        offsetX = 0.dp,
        offsetY = 1.dp
    ),
    Elevation2(
        color = Theme.color.black,
        alpha = 0.15f,
        shadowBlurRadius = 2.dp,
        offsetX = 0.dp,
        offsetY = 2.dp
    ),
    Elevation3(
        color = Theme.color.black,
        alpha = 0.15f,
        shadowBlurRadius = 3.dp,
        offsetX = 0.dp,
        offsetY = 3.dp
    ),
    Elevation4(
        color = Theme.color.black,
        alpha = 0.15f,
        shadowBlurRadius = 4.dp,
        offsetX = 0.dp,
        offsetY = 4.dp
    ),
    Elevation5(
        color = Theme.color.black,
        alpha = 0.15f,
        shadowBlurRadius = 5.dp,
        offsetX = 0.dp,
        offsetY = 5.dp
    ),
    MediumFloat1(
        color = Theme.color.primary.mono900,
        alpha = 0.1f,
        shadowBlurRadius = 4.dp,
        offsetX = 0.dp,
        offsetY = 4.dp
    ),
    MediumFloat2(
        color = Theme.color.primary.mono900,
        alpha = 0.15f,
        shadowBlurRadius = 4.dp,
        offsetX = 0.dp,
        offsetY = 4.dp
    ),
    MediumFloat3(
        color = Theme.color.primary.mono900,
        alpha = 0.2f,
        shadowBlurRadius = 4.dp,
        offsetX = 0.dp,
        offsetY = 4.dp
    ),
    MediumFloat4(
        color = Theme.color.primary.mono900,
        alpha = 0.25f,
        shadowBlurRadius = 4.dp,
        offsetX = 0.dp,
        offsetY = 4.dp
    ),
    MediumFloat5(
        color = Theme.color.primary.mono900,
        alpha = 0.3f,
        shadowBlurRadius = 4.dp,
        offsetX = 0.dp,
        offsetY = 4.dp
    ),
    SoftFloat1(
        color = Theme.color.primary.mono900,
        alpha = 0.04f,
        shadowBlurRadius = 3.dp,
        offsetX = 0.dp,
        offsetY = 0.dp
    ),
    SoftFloat2(
        color = Theme.color.primary.mono900,
        alpha = 0.06f,
        shadowBlurRadius = 3.dp,
        offsetX = 0.dp,
        offsetY = 0.dp
    ),
    SoftFloat3(
        color = Theme.color.primary.mono900,
        alpha = 0.08f,
        shadowBlurRadius = 3.dp,
        offsetX = 0.dp,
        offsetY = 0.dp
    ),
    SoftFloat4(
        color = Theme.color.primary.mono900,
        alpha = 0.1f,
        shadowBlurRadius = 3.dp,
        offsetX = 0.dp,
        offsetY = 0.dp
    ),
    SoftFloat5(
        color = Theme.color.primary.mono900,
        alpha = 0.12f,
        shadowBlurRadius = 3.dp,
        offsetX = 0.dp,
        offsetY = 0.dp
    )
}
