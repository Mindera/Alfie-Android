package au.com.alfie.ecomm.designsystem.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarPriority.NORMAL
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarTimeDuration.SHORT

data class SnackbarCustomVisuals(
    val type: SnackbarType,
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = true,
    val singleLine: Boolean = true,
    val timeDuration: SnackbarTimeDuration = SHORT,
    val priority: SnackbarPriority = NORMAL,
    @DrawableRes val icon: Int? = null,
    val onActionClick: () -> Unit = {}
) : SnackbarVisuals {

    override val duration: SnackbarDuration = SnackbarDuration.Indefinite
}
