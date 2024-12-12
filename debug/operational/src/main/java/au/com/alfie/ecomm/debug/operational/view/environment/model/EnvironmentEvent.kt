package au.com.alfie.ecomm.debug.operational.view.environment.model

internal sealed interface EnvironmentEvent {

    data class OnEnvironmentSelected(val environmentUI: EnvironmentUI) : EnvironmentEvent

    data class OnUrlChange(val url: String) : EnvironmentEvent

    data object OnSaveClick : EnvironmentEvent
}
