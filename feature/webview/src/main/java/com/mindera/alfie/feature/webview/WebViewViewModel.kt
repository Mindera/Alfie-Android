package com.mindera.alfie.feature.webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.deeplink.DeeplinkHandler
import com.mindera.alfie.core.navigation.arguments.webview.WebViewNavArgs
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.feature.webview.delegate.WebViewHandler
import com.mindera.alfie.feature.webview.delegate.WebViewHandlerDelegate
import com.mindera.alfie.feature.webview.factory.WebViewUIFactory
import com.mindera.alfie.feature.webview.model.WebViewUIState
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
