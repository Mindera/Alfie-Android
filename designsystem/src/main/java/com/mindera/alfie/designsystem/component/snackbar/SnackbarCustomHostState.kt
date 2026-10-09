package com.mindera.alfie.designsystem.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.mindera.alfie.designsystem.component.snackbar.SnackbarTimeDuration.INDEFINITE
import com.mindera.alfie.designsystem.component.snackbar.SnackbarTimeDuration.SHORT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

@Stable
class SnackbarCustomHostState {

    internal val hostState = SnackbarHostState()

    /**
     * The display currently on screen, or null when nothing is showing.
     *
     * Only ever touched from [Dispatchers.Main], which is what makes it safe without a lock.
     */
    private var displayJob: Job? = null

    suspend fun showSnackbar(
        type: SnackbarType,
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = true,
        singleLine: Boolean = true,
        timeDuration: SnackbarTimeDuration = SHORT,
        @DrawableRes icon: Int? = null,
        action: () -> Unit = {}
    ) {
        showSnackbar(
            SnackbarCustomVisuals.Snackbar(
                type = type,
                message = message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                singleLine = singleLine,
                timeDuration = timeDuration,
                icon = icon,
                onActionClick = action
            )
        )
    }

    /**
     * Shows [visuals], replacing whatever is on screen rather than queueing behind it.
     *
     * The newest message is the relevant one: a shopper who adds two items wants to see the second
     * confirmation, not the first one held over. Queueing also made a snackbar's own action button
     * feel broken, because the display outlived the tap that dismissed it.
     *
     * Returns once this snackbar is off screen — dismissed, actioned, replaced by a newer one, or
     * timed out, whichever comes first. It does not sit out the remaining duration after the
     * shopper has already dealt with it.
     */
    suspend fun showSnackbar(visuals: SnackbarCustomVisuals): Unit = withContext(Dispatchers.Main) {
        // Claimed before anything suspends: two callers that both read the old display and only
        // then published their own would each launch one, the earlier of which nothing could
        // cancel any more — it would hold Material's host and the later one would queue behind it.
        val previous = displayJob
        val display = launch {
            // Material's own host serialises on an internal mutex, so the outgoing snackbar has to
            // be fully cancelled before this one is shown — otherwise this one waits out the old
            // one's duration inside SnackbarHostState, with its own timeout already running.
            previous?.cancelAndJoin()

            val snackbar = launch { hostState.showSnackbar(visuals) }
            // `snackbar` completes the moment the shopper dismisses or taps the action; the
            // timeout is the other way out. Racing the two is what keeps a dealt-with snackbar
            // from holding the screen — and, before this class stopped blocking on it, the app.
            if (visuals.timeDuration == INDEFINITE) {
                snackbar.join()
            } else {
                withTimeoutOrNull(visuals.timeDuration.milliseconds) { snackbar.join() }
                snackbar.cancel()
            }
        }
        displayJob = display

        // A newer snackbar cancels `display`; cancelling a child is not a failure, so this caller
        // simply returns and the newer one owns the screen.
        display.join()
    }

    fun dismissSnackbar() {
        hostState.currentSnackbarData?.dismiss()
    }
}

@Composable
fun rememberSnackbarCustomHostState() = remember { SnackbarCustomHostState() }
