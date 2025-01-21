package au.com.alfie.ecomm.feature.webview.component

import android.annotation.SuppressLint
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonType.Primary
import au.com.alfie.ecomm.designsystem.component.loading.Loading
import au.com.alfie.ecomm.designsystem.component.loading.LoadingType
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.R
import au.com.alfie.ecomm.feature.webview.component.WebContent.NavigatorOnly
import au.com.alfie.ecomm.feature.webview.component.WebContent.Post
import au.com.alfie.ecomm.feature.webview.component.WebContent.Url
import au.com.alfie.ecomm.feature.webview.navigation.HistoryUpdate

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun WebView(
    state: WebViewState,
    navigator: WebViewNavigator,
    onLoadingOverride: (DeeplinkResult) -> Unit,
    onHistoryUpdate: (HistoryUpdate) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    client: WebViewClient = remember { WebViewClient() },
    chromeClient: WebChromeClient = remember { WebChromeClient() },
    isBackHandlerEnabled: Boolean = true
) {
    BoxWithConstraints(modifier) {
        val isInitialLoading = remember { mutableStateOf(true) }
        val isLoadFailed = remember { mutableStateOf(false) }

        val width = if (constraints.hasFixedWidth) MATCH_PARENT else WRAP_CONTENT
        val height = if (constraints.hasFixedHeight) MATCH_PARENT else WRAP_CONTENT
        val layoutParams = FrameLayout.LayoutParams(width, height)

        val webView = state.webView

        BackHandler(enabled = isBackHandlerEnabled) {
            if (webView?.canGoBack() == true) {
                webView.goBack()
            } else {
                onClose()
            }
        }

        webView?.let {
            LaunchedEffect(it, navigator) {
                navigator.handleNavigation(
                    webView = it,
                    onOverride = onLoadingOverride,
                    onHistoryUpdate = onHistoryUpdate
                )
            }

            LaunchedEffect(it, state) {
                snapshotFlow { state.content }.collect { content ->
                    when (content) {
                        is Url -> {
                            val uri = runCatching { Uri.parse(content.url) }.getOrNull()
                            val url = uri?.buildUpon()?.apply {
                                content.queryParameters.forEach { (key, value) ->
                                    appendQueryParameter(key, value)
                                }
                            }?.build().toString()

                            it.loadUrl(url, content.httpHeaders)
                        }
                        is Post -> {
                            it.postUrl(
                                content.url,
                                content.data
                            )
                        }
                        NavigatorOnly -> Unit
                    }
                }
            }

            LaunchedEffect(it, state) {
                snapshotFlow { state.loadingState }.collect { loadingState ->
                    isInitialLoading.value = loadingState !is LoadingState.Finished
                }
            }
        }
        if (!isLoadFailed.value) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        this.layoutParams = layoutParams
                        this.isVerticalScrollBarEnabled = false

                        state.viewState?.let {
                            this.restoreState(it)
                        }

                        client.state = state
                        client.navigator = navigator
                        chromeClient.state = state

                        webChromeClient = chromeClient
                        webViewClient = object : android.webkit.WebViewClient() {
                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                isLoadFailed.value = true
                            }

                            override fun onReceivedHttpError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                errorResponse: WebResourceResponse?
                            ) {
                                super.onReceivedHttpError(view, request, errorResponse)
                                isLoadFailed.value = true
                            }
                        }

                        settings.setSupportZoom(false)
                        settings.displayZoomControls = false
                        settings.javaScriptEnabled = true
                    }.also {
                        state.webView = it
                    }
                },
                modifier = modifier
            )
        }

        if (isLoadFailed.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.error_failed_to_load_page),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(Theme.spacing.spacing20))
                    Button(
                        type = Primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(PaddingValues(horizontal = Theme.spacing.spacing20)),
                        onClick = {
                            isLoadFailed.value = false
                            state.webView?.reload()
                        },
                        isEnabled = true,
                        text = stringResource(R.string.retry)
                    )
                }
            }
        }
        // Show loading dots while WebView is loading initial content
        else if (isInitialLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Loading(type = LoadingType.Dark)
            }
        }
    }
}
