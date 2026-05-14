package com.mindera.alfie.feature.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.deeplink.DeeplinkHandler
import com.mindera.alfie.feature.shop.delegate.NavigateToEntry
import com.mindera.alfie.feature.shop.delegate.NavigateToEntryDelegate
import com.mindera.alfie.feature.shop.model.ShopUIState
import com.mindera.alfie.feature.shop.model.ShopUIState.Data
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.feature.webview.delegate.WebViewHandler
import com.mindera.alfie.feature.webview.delegate.WebViewHandlerDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShopViewModel @Inject constructor(
    private val uiFactory: ShopUIFactory,
    val deeplinkHandler: DeeplinkHandler,
    navigateToEntryDelegate: NavigateToEntryDelegate,
    uiEventEmitterDelegate: UIEventEmitterDelegate,
    webViewHandlerDelegate: WebViewHandlerDelegate
) : ViewModel(),
    NavigateToEntry by navigateToEntryDelegate,
    UIEventEmitter by uiEventEmitterDelegate,
    WebViewHandler by webViewHandlerDelegate {

    private val _state = MutableStateFlow<ShopUIState>(Data(ShopUIFactory.PLACEHOLDER))
    val state: StateFlow<ShopUIState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = Data(shopUI = uiFactory())
        }
    }
}
