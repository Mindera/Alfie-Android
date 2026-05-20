package com.mindera.alfie.debug.operational.view.screen.model

internal sealed interface DebugScreenEvent {

    data object OpenFeedback : DebugScreenEvent
}
