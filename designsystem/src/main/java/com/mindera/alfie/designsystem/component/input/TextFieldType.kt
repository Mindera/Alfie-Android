package com.mindera.alfie.designsystem.component.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mindera.alfie.designsystem.tokens.LocalTheme

@Immutable
data class TextFieldColorSpec(
    val inputBorderColor: Color,
    val inputBorderFocusedColor: Color,
    val supportTextColor: Color,
    val supportIconColor: Color
)

enum class TextFieldType {
    Default,
    Error,
    Success
}

@Composable
fun TextFieldType.colorSpec(): TextFieldColorSpec {
    val c = LocalTheme.current.primitive.colors
    return when (this) {
        TextFieldType.Default -> TextFieldColorSpec(
            inputBorderColor = c.neutrals500,
            inputBorderFocusedColor = c.neutrals800,
            supportTextColor = c.neutrals500,
            supportIconColor = c.neutrals500
        )
        TextFieldType.Error -> TextFieldColorSpec(
            inputBorderColor = c.semanticError800,
            inputBorderFocusedColor = c.semanticError800,
            supportTextColor = c.semanticError800,
            supportIconColor = c.semanticError800
        )
        TextFieldType.Success -> TextFieldColorSpec(
            inputBorderColor = c.semanticSuccess800,
            inputBorderFocusedColor = c.semanticSuccess800,
            supportTextColor = c.semanticSuccess800,
            supportIconColor = c.semanticSuccess800
        )
    }
}
