package au.com.alfie.ecomm.designsystem.component.snackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun SnackbarCustomHost(snackbarCustomHostState: SnackbarCustomHostState) {
    SnackbarHost(
        modifier = Modifier
            .padding(bottom = Theme.spacing.spacing16)
            .padding(horizontal = Theme.spacing.spacing8),
        hostState = snackbarCustomHostState.hostState,
        snackbar = { snackbarData ->
            (snackbarData.visuals as? SnackbarCustomVisuals)?.let {
                Snackbar(
                    type = it.type,
                    message = it.message,
                    actionLabel = it.actionLabel,
                    withDismissAction = it.withDismissAction,
                    singleLine = it.singleLine,
                    icon = it.icon,
                    onActionClick = it.onActionClick,
                    onDismiss = { snackbarData.dismiss() }
                )
            }
        }
    )
}
