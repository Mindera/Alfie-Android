package au.com.alfie.ecomm.core.analytics.events

import au.com.alfie.ecomm.core.analytics.events.EventErrorValue.GENERIC_ERROR
import au.com.alfie.ecomm.core.analytics.events.EventErrorValue.NETWORK_ERROR
import au.com.alfie.ecomm.core.analytics.events.EventKey.ERROR_MESSAGE
import au.com.alfie.ecomm.core.analytics.events.EventKey.SCREEN_NAME

internal fun EventKey.toFirebaseEventKey(): String = when (this) {
    SCREEN_NAME -> "screen_name"
    ERROR_MESSAGE -> "error_message"
}

internal fun EventErrorValue.toFirebaseEventErrorValue(): String = when (this) {
    GENERIC_ERROR -> "generic_error"
    NETWORK_ERROR -> "network_error"
}
