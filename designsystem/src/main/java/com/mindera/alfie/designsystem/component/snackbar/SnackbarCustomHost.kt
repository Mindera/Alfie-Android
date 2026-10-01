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
            (snackbarData.visuals as? SnackbarCustomVisuals)?.let {
                if (it.type == SnackbarType.Toast) {
                    // The Toast spans the screen and sits flush on the bottom navigation, so it
                    // skips the margins a floating snackbar is inset by.
                    Toast(
                        message = it.message,
                        actionLabel = it.actionLabel,
                        onActionClick = it.onActionClick,
                        onDismiss = { snackbarData.dismiss() }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .padding(bottom = Theme.spacing.spacing16)
                            .padding(horizontal = Theme.spacing.spacing8)
                    ) {
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
            }
        }
    )
}
