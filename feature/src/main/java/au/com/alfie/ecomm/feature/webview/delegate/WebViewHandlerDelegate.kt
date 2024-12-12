package au.com.alfie.ecomm.feature.webview.delegate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import au.com.alfie.ecomm.feature.webview.WebViewEvent
import au.com.alfie.ecomm.feature.webview.navigation.HistoryNavigationHandler
import au.com.alfie.ecomm.feature.webview.navigation.HistoryNavigationResult
import au.com.alfie.ecomm.feature.webview.navigation.HistoryUpdate
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ViewModelScoped
class WebViewHandlerDelegate @Inject internal constructor(
    private val historyNavigationHandler: HistoryNavigationHandler,
    private val deeplinkHandler: DeeplinkHandler,
    private val uiEventEmitterDelegate: UIEventEmitterDelegate
) : WebViewHandler {

    override fun ViewModel.handleWebViewEvent(event: WebViewEvent) {
        when (event) {
            is WebViewEvent.Close -> close()
            is WebViewEvent.NavigateTo -> navigateTo(event.result)
            is WebViewEvent.OnHistoryUpdate -> onHistoryUpdate(event.result)
        }
    }

    private fun ViewModel.close() {
        runUIEvent { navigateBack() }
    }

    private fun ViewModel.navigateTo(result: DeeplinkResult) {
        viewModelScope.launch {
            when (result) {
                is DeeplinkResult.NavigateTo -> {
                    runUIEvent {
                        navigateTo(
                            direction = result.direction,
                            navOptions = result.navOptions
                        )
                    }
                }

                is DeeplinkResult.NavigateClearingStack -> {
                    runUIEvent {
                        navigateClearingStack(
                            direction = result.direction,
                            clearStartDestination = result.clearStartDestination,
                            launchSingleTop = result.launchSingleTop,
                            saveState = result.saveState,
                            restoreState = result.restoreState
                        )
                    }
                }

                is DeeplinkResult.Unresolved -> Timber.d("No destination for result: $result")
            }
        }
    }

    private fun ViewModel.onHistoryUpdate(historyUpdate: HistoryUpdate) {
        viewModelScope.launch {
            val navigationResult = historyNavigationHandler.resolve(
                toUrl = historyUpdate.to,
                fromUrl = historyUpdate.from
            )
            when (navigationResult) {
                HistoryNavigationResult.Close -> close()
                HistoryNavigationResult.Continue -> {
                    deeplinkHandler.resolve(historyUpdate.to)
                        .takeIf { result ->
                            when (result) {
                                is DeeplinkResult.NavigateTo -> result.direction.route.startsWith("web_view").not()
                                is DeeplinkResult.NavigateClearingStack -> result.direction.route.startsWith("web_view").not()
                                is DeeplinkResult.Unresolved -> false
                            }
                        }
                        ?.let {
                            runCatching { historyUpdate.cancelUpdate() }
                            navigateTo(it)
                        }
                }
            }
        }
    }

    private fun runUIEvent(block: UIEventEmitterDelegate.() -> Unit) {
        uiEventEmitterDelegate.run { block() }
    }
}
