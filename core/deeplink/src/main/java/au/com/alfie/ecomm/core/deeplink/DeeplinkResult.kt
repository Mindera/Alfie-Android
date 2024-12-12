package au.com.alfie.ecomm.core.deeplink

import androidx.navigation.NavOptionsBuilder
import com.ramcosta.composedestinations.spec.Direction

sealed interface DeeplinkResult {

    data class Unresolved(val url: String) : DeeplinkResult

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
