package au.com.alfie.ecomm.feature.webview.component

internal sealed interface NavigationEvent {

    data class LoadUrl(val url: String) : NavigationEvent

    data class UrlLoadingOverride(val url: String) : NavigationEvent

    data class OnUrlChange(val url: String) : NavigationEvent
}
