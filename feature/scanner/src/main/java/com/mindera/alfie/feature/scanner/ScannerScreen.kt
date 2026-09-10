package com.mindera.alfie.feature.scanner

import android.Manifest
import androidx.camera.core.ImageAnalysis
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.mindera.alfie.core.commons.util.IntentUtils
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomHostState
import com.mindera.alfie.designsystem.component.state.StateMessage
import com.mindera.alfie.designsystem.component.state.StateMessageAction
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.scanner.component.CameraPreview
import com.mindera.alfie.feature.scanner.component.ScannerCloseButton
import com.mindera.alfie.feature.scanner.component.ScannerOverlay
import com.mindera.alfie.feature.scanner.model.ScannerErrorType
import com.mindera.alfie.feature.scanner.model.ScannerEvent
import com.mindera.alfie.feature.scanner.model.ScannerUIState
import com.mindera.alfie.feature.uievent.handleUIEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
internal fun ScannerScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel: ScannerViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // The camera owns the whole content area; the shell's chrome would only get in the way.
    topBarState.hideTopBar()
    bottomBarState.hideBottomBar()

    viewModel.handleUIEvents(
        navigator = navigator,
        navController = navController,
        directionProvider = directionProvider,
        snackbarHostState = snackbarHostState
    )

    ScannerPermissionGate(onClose = { viewModel.handleEvent(ScannerEvent.OnCloseClick) }) {
        ScannerScreenContent(
            state = state,
            analyzer = viewModel.analyzer,
            onEvent = viewModel::handleEvent
        )
    }
}

@Composable
private fun ScannerScreenContent(
    state: ScannerUIState,
    analyzer: ImageAnalysis.Analyzer,
    onEvent: ClickEventOneArg<ScannerEvent>
) {
    when (state) {
        is ScannerUIState.Error -> ScannerError(type = state.type, onEvent = onEvent)
        ScannerUIState.Scanning,
        ScannerUIState.Detected -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalTheme.current.primitive.colors.neutrals900)
        ) {
            CameraPreview(
                analyzer = analyzer,
                // Frames stop reaching ML Kit the moment a barcode wins, while the preview
                // stays on screen through the navigation transition.
                isActive = state == ScannerUIState.Scanning,
                modifier = Modifier.fillMaxSize(),
                onBindFailure = { onEvent(ScannerEvent.OnCameraError) }
            )
            ScannerOverlay(onCloseClick = { onEvent(ScannerEvent.OnCloseClick) })
        }
    }
}

@Composable
private fun ScannerError(
    type: ScannerErrorType,
    onEvent: ClickEventOneArg<ScannerEvent>
) {
    when (type) {
        ScannerErrorType.CameraUnavailable -> ScannerMessage(
            title = stringResource(id = R.string.scanner_camera_unavailable_title),
            subtitle = stringResource(id = R.string.scanner_camera_unavailable_subtitle),
            action = StateMessageAction(
                label = stringResource(id = R.string.scanner_close),
                onClick = { onEvent(ScannerEvent.OnCloseClick) }
            ),
            onCloseClick = { onEvent(ScannerEvent.OnCloseClick) }
        )
    }
}

/**
 * A [StateMessage] plus the scanner's close affordance. The scanner hides both app bars, so a
 * message state without this would leave system back as the only escape.
 */
@Composable
private fun ScannerMessage(
    title: String,
    subtitle: String,
    action: StateMessageAction,
    onCloseClick: ClickEvent
) {
    Box(modifier = Modifier.fillMaxSize()) {
        StateMessage(
            title = title,
            subtitle = subtitle,
            action = action
        )
        ScannerCloseButton(
            onClick = onCloseClick,
            modifier = Modifier.align(Alignment.TopStart)
        )
    }
}

/**
 * Renders [content] only once the camera permission is granted.
 *
 * Accompanist exposes `isGranted` and `shouldShowRationale`, but those two alone cannot separate
 * "we have not asked yet" from "the user denied twice and the OS will no longer prompt" — both
 * report `false`/`false`. [hasAsked] is that missing third bit; without it the permanent-denial
 * message flashes on screen before the very first system dialog appears.
 *
 * Permission state stays here rather than in [ScannerUIState] so the ViewModel holds no Android
 * permission APIs and remains unit-testable.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ScannerPermissionGate(
    onClose: ClickEvent,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val permission = rememberPermissionState(Manifest.permission.CAMERA)
    var hasAsked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!permission.status.isGranted && !hasAsked) {
            hasAsked = true
            permission.launchPermissionRequest()
        }
    }

    when {
        permission.status.isGranted -> content()

        permission.status.shouldShowRationale -> ScannerMessage(
            title = stringResource(id = R.string.scanner_permission_rationale_title),
            subtitle = stringResource(id = R.string.scanner_permission_rationale_subtitle),
            action = StateMessageAction(
                label = stringResource(id = R.string.scanner_permission_allow),
                onClick = permission::launchPermissionRequest
            ),
            onCloseClick = onClose
        )

        hasAsked -> ScannerMessage(
            title = stringResource(id = R.string.scanner_permission_denied_title),
            subtitle = stringResource(id = R.string.scanner_permission_denied_subtitle),
            action = StateMessageAction(
                label = stringResource(id = R.string.scanner_permission_open_settings),
                onClick = { IntentUtils.openAppSettings(context) }
            ),
            onCloseClick = onClose
        )

        // The first request is in flight: stay blank rather than flash a denial state.
        else -> Box(modifier = Modifier.fillMaxSize())
    }
}
