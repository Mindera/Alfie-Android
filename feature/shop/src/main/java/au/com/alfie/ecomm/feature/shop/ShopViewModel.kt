package au.com.alfie.ecomm.feature.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.feature.shop.delegate.NavigateToEntry
import au.com.alfie.ecomm.feature.shop.delegate.NavigateToEntryDelegate
import au.com.alfie.ecomm.feature.shop.model.ShopUIState
import au.com.alfie.ecomm.feature.shop.model.ShopUIState.Data
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import au.com.alfie.ecomm.feature.webview.delegate.WebViewHandler
import au.com.alfie.ecomm.feature.webview.delegate.WebViewHandlerDelegate
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
