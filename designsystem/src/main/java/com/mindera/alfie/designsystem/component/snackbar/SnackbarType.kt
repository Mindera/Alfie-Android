package com.mindera.alfie.designsystem.component.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mindera.alfie.designsystem.tokens.LocalTheme

@Immutable
data class SnackbarColorSpec(
    val backgroundColor: Color,
    val contentColor: Color
)

enum class SnackbarType {
    Info,
    Success,
    Error
}

@Composable
fun SnackbarType.colorSpec(): SnackbarColorSpec {
    val c = LocalTheme.current.primitive.colors
    return when (this) {
        SnackbarType.Info -> SnackbarColorSpec(
            backgroundColor = c.neutrals700,
            contentColor = c.neutrals0
        )
        SnackbarType.Success -> SnackbarColorSpec(
            backgroundColor = c.semanticSuccess100,
            contentColor = c.semanticSuccess800
        )
        SnackbarType.Error -> SnackbarColorSpec(
            backgroundColor = c.semanticError100,
            contentColor = c.semanticError800
        )
    }
}
