package com.mindera.alfie.core.analytics.events

import com.mindera.alfie.core.analytics.events.EventErrorValue.AUTHENTICATION
import com.mindera.alfie.core.analytics.events.EventErrorValue.GENERIC_ERROR
import com.mindera.alfie.core.analytics.events.EventErrorValue.NETWORK_ERROR
import com.mindera.alfie.core.analytics.events.EventErrorValue.SERVER_ERROR
import com.mindera.alfie.core.analytics.events.EventErrorValue.THROTTLED
import com.mindera.alfie.core.analytics.events.EventErrorValue.TIMEOUT
import com.mindera.alfie.core.analytics.events.EventKey.ERROR_MESSAGE
import com.mindera.alfie.core.analytics.events.EventKey.SCREEN_NAME

internal fun EventKey.toFirebaseEventKey(): String = when (this) {
    SCREEN_NAME -> "screen_name"
    ERROR_MESSAGE -> "error_message"
}

internal fun EventErrorValue.toFirebaseEventErrorValue(): String = when (this) {
    GENERIC_ERROR -> "generic_error"
    NETWORK_ERROR -> "network_error"
    THROTTLED -> "throttled"
    TIMEOUT -> "timeout"
    SERVER_ERROR -> "server_error"
    AUTHENTICATION -> "authentication"
}
