package com.mindera.alfie.feature.account.model

import com.mindera.alfie.feature.uievent.UIEvent

internal sealed interface AccountEvent {

    data class OpenEntry(val uiEvent: UIEvent) : AccountEvent
}
