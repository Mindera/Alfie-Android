package au.com.alfie.ecomm.feature.uievent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptionsBuilder
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomVisuals
import com.ramcosta.composedestinations.spec.Direction
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
class UIEventEmitterDelegate @Inject constructor() : UIEventEmitter {

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    override val uiEvent: SharedFlow<UIEvent> = _uiEvent.asSharedFlow()

    override fun ViewModel.emitUIEvent(uiEvent: UIEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }

    override fun ViewModel.navigateTo(
        direction: Direction,
        navOptions: NavOptionsBuilder.() -> Unit
    ) {
        val event = UIEvent.Base.NavigateToDirection(
            direction = direction,
            navOptions = navOptions
        )
        emitUIEvent(event)
    }

    override fun ViewModel.navigateTo(
        screen: Screen,
        navOptions: NavOptionsBuilder.() -> Unit
    ) {
        val event = UIEvent.Base.NavigateToScreen(
            screen = screen,
            navOptions = navOptions
        )
        emitUIEvent(event)
    }

    override fun ViewModel.navigateClearingStack(
        direction: Direction,
        clearStartDestination: Boolean,
        launchSingleTop: Boolean,
        saveState: Boolean,
        restoreState: Boolean
    ) {
        val event = UIEvent.Base.NavigateToDirectionClearingStack(
            direction = direction,
            clearStartDestination = clearStartDestination,
            launchSingleTop = launchSingleTop,
            saveState = saveState,
            restoreState = restoreState
        )
        emitUIEvent(event)
    }

    override fun ViewModel.navigateClearingStack(
        screen: Screen,
        clearStartDestination: Boolean,
        launchSingleTop: Boolean,
        saveState: Boolean,
        restoreState: Boolean
    ) {
        val event = UIEvent.Base.NavigateToScreenClearingStack(
            screen = screen,
            clearStartDestination = clearStartDestination,
            launchSingleTop = launchSingleTop,
            saveState = saveState,
            restoreState = restoreState
        )
        emitUIEvent(event)
    }

    override fun ViewModel.navigateBack() {
        val event = UIEvent.Base.NavigateBack
        emitUIEvent(event)
    }

    override fun ViewModel.showSnackbar(visuals: SnackbarCustomVisuals) {
        val event = UIEvent.Base.ShowSnackbar(visuals = visuals)
        emitUIEvent(event)
    }
}
