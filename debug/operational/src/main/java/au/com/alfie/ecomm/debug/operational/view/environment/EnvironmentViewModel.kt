package au.com.alfie.ecomm.debug.operational.view.environment

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.core.environment.model.Environment
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentEvent
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentState
import au.com.alfie.ecomm.debug.operational.view.environment.model.EnvironmentUIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EnvironmentViewModel @Inject constructor(
    private val environmentUIFactory: EnvironmentUIFactory,
    private val environmentManager: EnvironmentManager
) : ViewModel() {

    private val _state: MutableStateFlow<EnvironmentState> = MutableStateFlow(EnvironmentState.Empty)
    val state: StateFlow<EnvironmentState> = _state

    private val _uiEvent = MutableSharedFlow<EnvironmentUIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var environmentSelected: Environment? = null

    init {
        viewModelScope.launch {
            val environments = environmentUIFactory()
            _state.value = EnvironmentState.Data(environments)
            environmentSelected = environments.firstOrNull { it.isSelected }?.environment
        }
    }

    fun handleEvent(event: EnvironmentEvent) {
        when (event) {
            is EnvironmentEvent.OnEnvironmentSelected -> environmentSelected = event.environmentUI.environment
            is EnvironmentEvent.OnUrlChange -> onUrlChange(event.url)
            is EnvironmentEvent.OnSaveClick -> saveEnvironment()
        }
    }

    fun restart(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val mainIntent = Intent.makeRestartActivityTask(intent?.component)
        mainIntent.setPackage(context.packageName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }

    private fun onUrlChange(url: String) {
        (environmentSelected as? Environment.Custom)?.let {
            environmentSelected = it.copy(url)
        }
    }

    private fun saveEnvironment() {
        viewModelScope.launch {
            if (environmentSelected?.graphQLUrl.isNullOrBlank()) {
                _uiEvent.emit(EnvironmentUIEvent.ShowErrorSnackbar)
            } else {
                environmentSelected?.let {
                    environmentManager.update(it)
                    _uiEvent.emit(EnvironmentUIEvent.ShowSuccessSnackbar)
                    _uiEvent.emit(EnvironmentUIEvent.Restart)
                }
            }
        }
    }
}
