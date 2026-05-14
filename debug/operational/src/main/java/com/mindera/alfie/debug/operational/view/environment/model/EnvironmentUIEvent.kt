package com.mindera.alfie.debug.operational.view.environment.model

internal sealed interface EnvironmentUIEvent {

    data object Restart : EnvironmentUIEvent

    data object ShowSuccessSnackbar : EnvironmentUIEvent

    data object ShowErrorSnackbar : EnvironmentUIEvent
}
