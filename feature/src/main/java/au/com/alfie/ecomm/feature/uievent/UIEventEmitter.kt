package au.com.alfie.ecomm.feature.uievent

import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptionsBuilder
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomVisuals
import com.ramcosta.composedestinations.spec.Direction
import kotlinx.coroutines.flow.SharedFlow

interface UIEventEmitter {

    val uiEvent: SharedFlow<UIEvent>

    fun ViewModel.emitUIEvent(uiEvent: UIEvent)

    fun ViewModel.navigateTo(
        direction: Direction,
        navOptions: NavOptionsBuilder.() -> Unit = {}
    )

    fun ViewModel.navigateTo(
        screen: Screen,
        navOptions: NavOptionsBuilder.() -> Unit = {}
    )

    fun ViewModel.navigateClearingStack(
        direction: Direction,
        clearStartDestination: Boolean = false,
        launchSingleTop: Boolean = false,
        saveState: Boolean = false,
        restoreState: Boolean = false
    )

    fun ViewModel.navigateClearingStack(
        screen: Screen,
        clearStartDestination: Boolean = false,
        launchSingleTop: Boolean = false,
        saveState: Boolean = false,
        restoreState: Boolean = false
    )

    fun ViewModel.navigateBack()

    fun ViewModel.showSnackbar(visuals: SnackbarCustomVisuals)
}
