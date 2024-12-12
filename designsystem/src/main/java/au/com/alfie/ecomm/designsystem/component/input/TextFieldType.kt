package au.com.alfie.ecomm.designsystem.component.input

import androidx.compose.ui.graphics.Color
import au.com.alfie.ecomm.designsystem.theme.Theme

enum class TextFieldType(
    val inputBorderColor: Color,
    val inputBorderFocusedColor: Color,
    val supportTextColor: Color,
    val supportIconColor: Color
) {
    Default(
        inputBorderColor = Theme.color.primary.mono500,
        inputBorderFocusedColor = Theme.color.primary.mono900,
        supportTextColor = Theme.color.primary.mono500,
        supportIconColor = Theme.color.primary.mono500
    ),
    Error(
        inputBorderColor = Theme.color.secondary.red800,
        inputBorderFocusedColor = Theme.color.secondary.red800,
        supportTextColor = Theme.color.secondary.red800,
        supportIconColor = Theme.color.secondary.red800
    ),
    Success(
        inputBorderColor = Theme.color.secondary.green800,
        inputBorderFocusedColor = Theme.color.secondary.green800,
        supportTextColor = Theme.color.secondary.green800,
        supportIconColor = Theme.color.secondary.green800
    )
}
