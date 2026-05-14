package com.mindera.alfie.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.feature.account.factory.AccountUIFactory
import com.mindera.alfie.feature.account.model.AccountEvent
import com.mindera.alfie.feature.account.model.AccountEvent.OpenEntry
import com.mindera.alfie.feature.account.model.AccountUIState
import com.mindera.alfie.feature.account.model.AccountUIState.Loaded
import com.mindera.alfie.feature.account.model.AccountUIState.Loading
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
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
