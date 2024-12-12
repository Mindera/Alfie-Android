package au.com.alfie.ecomm.feature.webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.navigation.arguments.webview.WebViewNavArgs
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import au.com.alfie.ecomm.feature.webview.delegate.WebViewHandler
import au.com.alfie.ecomm.feature.webview.delegate.WebViewHandlerDelegate
import au.com.alfie.ecomm.feature.webview.factory.WebViewUIFactory
import au.com.alfie.ecomm.feature.webview.model.WebViewUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WebViewViewModel @Inject constructor(
    private val uiFactory: WebViewUIFactory,
    val deeplinkHandler: DeeplinkHandler,
    savedStateHandle: SavedStateHandle,
    uiEventEmitterDelegate: UIEventEmitterDelegate,
    webViewHandlerDelegate: WebViewHandlerDelegate
) : ViewModel(), UIEventEmitter by uiEventEmitterDelegate, WebViewHandler by webViewHandlerDelegate {

    private val _state = MutableStateFlow<WebViewUIState>(WebViewUIState.Loading)
    val state = _state.asStateFlow()

    private val args: WebViewNavArgs = savedStateHandle.navArgs()
    private val url = args.url
    val topBarTitle = args.title
    val isTitleLeftAligned = args.isTitleLeftAligned
    val isBottomBarItem = args.isBottomBarItem
    val hasSearchAction = args.hasSearchAction
    val hasWishlistAction = args.hasWishlistAction
    val hasAccountAction = args.hasAccountAction

    init {
        loadContent()
    }

    private fun loadContent() = viewModelScope.launch {
        if (url.isNotBlank()) {
            val content = uiFactory(
                url = url,
                queryParameters = emptyMap(),
                headers = emptyMap()
            )
            _state.value = WebViewUIState.Loaded(content)
        } else {
            _state.value = WebViewUIState.Error
        }
    }
}
