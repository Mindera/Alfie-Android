package au.com.alfie.ecomm.debug.operational.view.environment.model

internal sealed interface EnvironmentState {

    data object Empty : EnvironmentState

    data class Data(val environments: List<EnvironmentUI>) : EnvironmentState
}
