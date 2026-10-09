package com.mindera.alfie.designsystem.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import com.mindera.alfie.designsystem.component.snackbar.SnackbarTimeDuration.SHORT

/**
 * What [SnackbarCustomHost] can show. Each case carries only the fields its component draws, so a
 * caller cannot pass a value that is silently ignored.
 */
sealed interface SnackbarCustomVisuals : SnackbarVisuals {
    val timeDuration: SnackbarTimeDuration
    val onActionClick: () -> Unit

    override val duration: SnackbarDuration
        get() = SnackbarDuration.Indefinite

    /** The floating [Snackbar], inset from the screen edges. */
    data class Snackbar(
        val type: SnackbarType,
        override val message: String,
        override val actionLabel: String? = null,
        override val withDismissAction: Boolean = true,
        val singleLine: Boolean = true,
        override val timeDuration: SnackbarTimeDuration = SHORT,
        @DrawableRes val icon: Int? = null,
        override val onActionClick: () -> Unit = {}
    ) : SnackbarCustomVisuals

    /** The DS [Toast]: full width on the bottom navigation, no icon and no dismiss button. */
    data class Toast(
        override val message: String,
        override val actionLabel: String? = null,
        override val timeDuration: SnackbarTimeDuration = SHORT,
        override val onActionClick: () -> Unit = {}
    ) : SnackbarCustomVisuals {
        override val withDismissAction: Boolean = false
    }
}
