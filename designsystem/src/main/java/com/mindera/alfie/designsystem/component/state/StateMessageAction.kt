package com.mindera.alfie.designsystem.component.state

import com.mindera.alfie.core.ui.event.ClickEvent

/**
 * Optional call-to-action rendered below a [StateMessage].
 */
data class StateMessageAction(
    val label: String,
    val onClick: ClickEvent
)
