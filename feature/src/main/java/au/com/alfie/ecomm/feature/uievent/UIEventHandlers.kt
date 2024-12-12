package au.com.alfie.ecomm.feature.uievent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomHostState
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate

@Composable
fun UIEventEmitter.handleUIEvents(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    onCustomEvent: (UIEvent.Custom) -> Unit = { },
    onBaseEventOverride: ((UIEvent.Base) -> Unit)? = null
) {
    LaunchedEffect(Unit) {
        uiEvent.collect { uiEvent ->
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
