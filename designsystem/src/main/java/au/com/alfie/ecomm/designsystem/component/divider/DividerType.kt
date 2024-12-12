package au.com.alfie.ecomm.designsystem.component.divider

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.theme.Theme

enum class DividerType(
    val color: Color,
    val thickness: Dp
) {
    Solid1Mono100(
        color = Theme.color.primary.mono100,
        thickness = 1.dp
    ),
    Solid1Mono200(
        color = Theme.color.primary.mono200,
        thickness = 1.dp
    ),
    Solid1Mono300(
        color = Theme.color.primary.mono300,
        thickness = 1.dp
    ),
    Solid2Mono100(
        color = Theme.color.primary.mono100,
        thickness = 2.dp
    )
}
