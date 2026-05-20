package com.mindera.alfie.feature.debug

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun DebugScreen(
    navigator: DestinationsNavigator,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel = hiltViewModel<DebugViewModel>()

    topBarState.textTopBar(stringResource(id = R.string.debug_title))
    bottomBarState.hideBottomBar()

    viewModel.debugViewContent.content(navigator).invoke()
}
