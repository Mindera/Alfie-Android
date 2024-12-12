package au.com.alfie.ecomm.designsystem.component.snackbar

import androidx.compose.ui.graphics.Color
import au.com.alfie.ecomm.designsystem.theme.Theme

enum class SnackbarType(
    val backgroundColor: Color,
    val contentColor: Color
) {
    Info(
        backgroundColor = Theme.color.primary.mono800,
        contentColor = Theme.color.white
    ),
    Success(
        backgroundColor = Theme.color.secondary.green100,
        contentColor = Theme.color.secondary.green800
    ),
    Error(
        backgroundColor = Theme.color.secondary.red100,
        contentColor = Theme.color.secondary.red800
    )
}
