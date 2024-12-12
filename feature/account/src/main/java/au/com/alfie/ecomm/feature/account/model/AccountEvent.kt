package au.com.alfie.ecomm.feature.account.model

import au.com.alfie.ecomm.feature.uievent.UIEvent

internal sealed interface AccountEvent {

    data class OpenEntry(val uiEvent: UIEvent) : AccountEvent
}
