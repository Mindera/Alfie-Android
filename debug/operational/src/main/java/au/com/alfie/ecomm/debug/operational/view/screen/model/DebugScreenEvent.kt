package au.com.alfie.ecomm.debug.operational.view.screen.model

internal sealed interface DebugScreenEvent {

    data object OpenFeedback : DebugScreenEvent
}
