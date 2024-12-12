package au.com.alfie.ecomm.designsystem.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarPriority.HIGH
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarPriority.NORMAL
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarTimeDuration.INDEFINITE
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarTimeDuration.SHORT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@Stable
class SnackbarCustomHostState {

    internal val hostState = SnackbarHostState()
    private val mutex = Mutex()
    private val parentJob = Job()

    suspend fun showSnackbar(
        type: SnackbarType,
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = true,
        singleLine: Boolean = true,
        timeDuration: SnackbarTimeDuration = SHORT,
        priority: SnackbarPriority = NORMAL,
        @DrawableRes icon: Int? = null,
        action: () -> Unit = {}
    ) {
        showSnackbar(
            SnackbarCustomVisuals(
                type = type,
                message = message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                singleLine = singleLine,
                timeDuration = timeDuration,
                priority = priority,
                icon = icon,
                onActionClick = action
            )
        )
    }

    suspend fun showSnackbar(visuals: SnackbarCustomVisuals) {
        if (visuals.priority == HIGH) {
            // cancels all the snackbars in the queue
            parentJob.cancelChildren()
        }

        withContext(Dispatchers.Main + parentJob) {
            mutex.withLock {
                val snackbarJob = launch { hostState.showSnackbar(visuals) }
                if (visuals.timeDuration != INDEFINITE) {
                    delay(visuals.timeDuration.milliseconds)
                    snackbarJob.cancel()
                }
            }
        }
    }

    fun dismissSnackbar() {
        hostState.currentSnackbarData?.dismiss()
    }
}

@Composable
fun rememberSnackbarCustomHostState() = remember { SnackbarCustomHostState() }
