package au.com.alfie.ecomm.designsystem.component.loading

import androidx.compose.ui.graphics.Color
import au.com.alfie.ecomm.designsystem.theme.Theme

enum class LoadingType(
    val color: Color
) {
    Dark(color = Theme.color.primary.mono900),
    Light(color = Theme.color.white)
}
