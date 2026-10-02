package com.mindera.alfie.designsystem.component.snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindera.alfie.designsystem.theme.Theme

@Composable
fun SnackbarCustomHost(snackbarCustomHostState: SnackbarCustomHostState) {
    SnackbarHost(
        hostState = snackbarCustomHostState.hostState,
        snackbar = { snackbarData ->
            when (val visuals = snackbarData.visuals) {
                // The Toast spans the screen and sits flush on the bottom navigation, so it skips
                // the margins a floating snackbar is inset by.
                is SnackbarCustomVisuals.Toast -> Toast(
                    message = visuals.message,
                    actionLabel = visuals.actionLabel,
                    onActionClick = visuals.onActionClick,
                    onDismiss = { snackbarData.dismiss() }
                )
                is SnackbarCustomVisuals.Snackbar -> Box(
                    modifier = Modifier
                        .padding(bottom = Theme.spacing.spacing16)
                        .padding(horizontal = Theme.spacing.spacing8)
                ) {
                    Snackbar(
                        type = visuals.type,
                        message = visuals.message,
                        actionLabel = visuals.actionLabel,
                        withDismissAction = visuals.withDismissAction,
                        singleLine = visuals.singleLine,
                        icon = visuals.icon,
                        onActionClick = visuals.onActionClick,
                        onDismiss = { snackbarData.dismiss() }
                    )
                }
                // Only this module builds visuals for the host, so nothing else reaches it.
                else -> Unit
            }
        }
    )
}
