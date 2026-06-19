package com.mindera.alfie.core.deeplink

import androidx.annotation.StringRes
import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.spec.Direction

sealed interface DeeplinkResult {

    data class Unresolved(val url: String) : DeeplinkResult

    /**
     * The deeplink was recognised but its payload is invalid (e.g. an empty search query).
     * The host surfaces [messageRes] to the user (snackbar) and performs no navigation.
     */
    data class ShowError(@StringRes val messageRes: Int) : DeeplinkResult

    data class NavigateTo(
        val direction: Direction,
        val navOptions: NavOptionsBuilder.() -> Unit = {}
    ) : DeeplinkResult

    data class NavigateClearingStack(
        val direction: Direction,
        val clearStartDestination: Boolean = false,
        val launchSingleTop: Boolean = false,
        val saveState: Boolean = false,
        val restoreState: Boolean = false
    ) : DeeplinkResult
}
