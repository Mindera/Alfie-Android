package au.com.alfie.ecomm.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.feature.account.factory.AccountUIFactory
import au.com.alfie.ecomm.feature.account.model.AccountEvent
import au.com.alfie.ecomm.feature.account.model.AccountEvent.OpenEntry
import au.com.alfie.ecomm.feature.account.model.AccountUIState
import au.com.alfie.ecomm.feature.account.model.AccountUIState.Loaded
import au.com.alfie.ecomm.feature.account.model.AccountUIState.Loading
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AccountViewModel @Inject constructor(
    private val accountUIFactory: AccountUIFactory,
    uiEventEmitterDelegate: UIEventEmitterDelegate
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate {

    private val _state: MutableStateFlow<AccountUIState> = MutableStateFlow(Loading)
    val state = _state.asStateFlow()

    init {
        loadAccount()
    }

    fun handleEvent(event: AccountEvent) {
        when (event) {
            is OpenEntry -> openAccountEntry(event = event)
        }
    }

    private fun loadAccount() {
        viewModelScope.launch(Dispatchers.Default) {
            _state.value = Loading

            val accountUI = accountUIFactory()
            _state.value = Loaded(accountUI = accountUI)
        }
    }

    private fun openAccountEntry(event: OpenEntry) {
        emitUIEvent(event.uiEvent)
    }
}
