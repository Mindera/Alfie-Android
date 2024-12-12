package au.com.alfie.ecomm.debug.operational.view.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.debug.operational.R
import au.com.alfie.ecomm.debug.operational.view.screen.model.DebugScreenEvent
import au.com.alfie.ecomm.debug.operational.view.screen.model.DebugScreenOpUI
import com.google.firebase.Firebase
import com.google.firebase.appdistribution.appDistribution
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DebugScreenOpViewModel @Inject constructor(
    debugScreenOpFactory: DebugScreenOpFactory
) : ViewModel() {

    private val _state = MutableStateFlow(emptyList<DebugScreenOpUI>())
    val state: StateFlow<List<DebugScreenOpUI>> = _state

    init {
        viewModelScope.launch {
            _state.value = debugScreenOpFactory()
        }
    }

    fun handleEvents(event: DebugScreenEvent) {
        when (event) {
            DebugScreenEvent.OpenFeedback -> Firebase.appDistribution.startFeedback(R.string.feedback_message)
        }
    }
}
