package com.mindera.alfie.feature.uievent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomHostState
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun UIEventEmitter.handleUIEvents(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    onCustomEvent: (UIEvent.Custom) -> Unit = { },
    onBaseEventOverride: ((UIEvent.Base) -> Unit)? = null
) {
    val snackbarScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        uiEvent.collectUIEvents(snackbarScope = snackbarScope) { uiEvent ->
            uiEvent.handleUIEvent(
                navigator = navigator,
                navController = navController,
                directionProvider = directionProvider,
                snackbarHostState = snackbarHostState,
                onCustomEvent = onCustomEvent,
                onBaseEventOverride = onBaseEventOverride
            )
        }
    }
}

/**
 * Hands each event to [handle] in order, except snackbars, which are handled in [snackbarScope].
 *
 * Showing a snackbar suspends for as long as it is on screen — up to its full duration when the
 * shopper leaves it alone. [UIEventEmitter.uiEvent] is an unbuffered SharedFlow, so an emit does
 * not complete until this collector takes it; handling a snackbar inline would hold every event
 * emitted meanwhile, including the navigation the snackbar's own action emits ("View Wishlist"
 * left the shopper in place until the toast timed out). AppNavigation launches its deeplink-error
 * snackbars separately for the same reason.
 */
internal suspend fun Flow<UIEvent>.collectUIEvents(
    snackbarScope: CoroutineScope,
    handle: suspend (UIEvent) -> Unit
) {
    collect { uiEvent ->
        if (uiEvent is UIEvent.Base.ShowSnackbar) {
            snackbarScope.launch { handle(uiEvent) }
        } else {
            handle(uiEvent)
        }
    }
}

@Composable
fun UIEventEmitter.handleUIEvents(onEvent: (UIEvent) -> Unit) {
    LaunchedEffect(Unit) {
        uiEvent.collect { uiEvent ->
            onEvent(uiEvent)
        }
    }
}

suspend fun UIEvent.handleUIEvent(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    onCustomEvent: (UIEvent.Custom) -> Unit = { },
    onBaseEventOverride: ((UIEvent.Base) -> Unit)? = null
) {
    when (this) {
        is UIEvent.Base -> onBaseEventOverride?.invoke(this) ?: this.handleBaseEvent(
            navigator = navigator,
            navController = navController,
            directionProvider = directionProvider,
            snackbarHostState = snackbarHostState
        )
        is UIEvent.Custom -> onCustomEvent(this)
    }
}

internal suspend fun UIEvent.Base.handleBaseEvent(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState
) {
    when (this) {
        is UIEvent.Base.NavigateToDirection -> this.handle(navigator)
        is UIEvent.Base.NavigateToScreen -> this.handle(navigator, directionProvider)
        is UIEvent.Base.NavigateToDirectionClearingStack -> this.handle(navController)
        is UIEvent.Base.NavigateToScreenClearingStack -> this.handle(navController, directionProvider)
        is UIEvent.Base.NavigateBack -> this.handle(navigator)
        is UIEvent.Base.ShowSnackbar -> this.handle(snackbarHostState)
    }
}

fun UIEvent.Base.NavigateToDirection.handle(
    navigator: DestinationsNavigator
) {
    navigator.navigate(
        direction = direction,
        builder = navOptions
    )
}

fun UIEvent.Base.NavigateToDirection.handle(
    navController: NavController
) {
    navController.navigate(
        direction = direction,
        navOptionsBuilder = navOptions
    )
}

fun UIEvent.Base.NavigateToScreen.handle(
    navigator: DestinationsNavigator,
    directionProvider: DirectionProvider
) {
    val direction = directionProvider.fromScreen(screen)
    navigator.navigate(
        direction = direction,
        builder = navOptions
    )
}

fun UIEvent.Base.NavigateToScreen.handle(
    navController: NavController,
    directionProvider: DirectionProvider
) {
    val direction = directionProvider.fromScreen(screen)
    navController.navigate(
        direction = direction,
        navOptionsBuilder = navOptions
    )
}

fun UIEvent.Base.NavigateToDirectionClearingStack.handle(
    navController: NavController
) {
    navController.popBackStack(
        destinationId = navController.graph.findStartDestination().id,
        inclusive = this.clearStartDestination
    )
    navController.navigate(
        direction = this.direction,
        navOptionsBuilder = {
            launchSingleTop = this@handle.launchSingleTop
            restoreState = this@handle.restoreState
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = this@handle.clearStartDestination
                saveState = this@handle.saveState
            }
        }
    )
}

fun UIEvent.Base.NavigateToScreenClearingStack.handle(
    navController: NavController,
    directionProvider: DirectionProvider
) {
    val direction = directionProvider.fromScreen(screen)
    navController.popBackStack(
        destinationId = navController.graph.findStartDestination().id,
        inclusive = this.clearStartDestination
    )
    navController.navigate(
        direction = direction,
        navOptionsBuilder = {
            launchSingleTop = this@handle.launchSingleTop
            restoreState = this@handle.restoreState
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = this@handle.clearStartDestination
                saveState = this@handle.saveState
            }
        }
    )
}

fun UIEvent.Base.NavigateBack.handle(
    navigator: DestinationsNavigator
) {
    navigator.navigateUp()
}

suspend fun UIEvent.Base.ShowSnackbar.handle(
    snackbarHostState: SnackbarCustomHostState
) {
    snackbarHostState.showSnackbar(visuals)
}
