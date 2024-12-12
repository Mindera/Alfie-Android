package au.com.alfie.ecomm.feature.uievent

import androidx.navigation.NavOptionsBuilder
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomVisuals
import com.ramcosta.composedestinations.spec.Direction

sealed interface UIEvent {

    interface Custom : UIEvent

    sealed interface Base : UIEvent {

        data class NavigateToDirection(
            val direction: Direction,
            val navOptions: NavOptionsBuilder.() -> Unit = {}
        ) : Base

        data class NavigateToScreen(
            val screen: Screen,
            val navOptions: NavOptionsBuilder.() -> Unit = {}
        ) : Base

        data class NavigateToDirectionClearingStack(
            val direction: Direction,
            val clearStartDestination: Boolean = false,
            val launchSingleTop: Boolean = false,
            val saveState: Boolean = false,
            val restoreState: Boolean = false
        ) : Base

        data class NavigateToScreenClearingStack(
            val screen: Screen,
            val clearStartDestination: Boolean = false,
            val launchSingleTop: Boolean = false,
            val saveState: Boolean = false,
            val restoreState: Boolean = false
        ) : Base

        data object NavigateBack : Base

        data class ShowSnackbar(
            val visuals: SnackbarCustomVisuals
        ) : Base
    }
}
