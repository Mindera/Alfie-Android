package au.com.alfie.ecomm.feature.webview.component

internal sealed interface LoadingState {

    data object Initializing : LoadingState

    data class Loading(val progress: Float) : LoadingState

    data object Finished : LoadingState
}
