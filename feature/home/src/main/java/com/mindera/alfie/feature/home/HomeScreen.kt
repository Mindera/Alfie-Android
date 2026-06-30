package com.mindera.alfie.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.debug.runner.LocalDebugComposeRunner
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.searchbar.rememberSearchState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.action.TopBarAction
import com.mindera.alfie.designsystem.component.topbar.custom.LandingHeader
import com.mindera.alfie.designsystem.component.topbar.custom.LandingHeaderType
import com.mindera.alfie.designsystem.component.topbar.scope.TopBarScope
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.feature.home.model.HomeUI
import com.mindera.alfie.feature.home.model.HomeUIState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.toImmutableList
import com.mindera.alfie.designsystem.icons.AlfieIcons

private const val SCREEN_CONTENT_HEIGHT = 100

@Destination
@Composable
internal fun HomeScreen(
    topBarState: TopBarState,
    bottomBarState: BottomBarState,
    navigator: DestinationsNavigator,
    directionProvider: DirectionProvider
) {
    bottomBarState.showBottomBar()

    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val actions = buildList {
        LocalDebugComposeRunner.current {
            add(
                TopBarAction.Debug { navigator.navigate(directionProvider.fromScreen(Screen.Debug)) }
            )
        }
        add(
            TopBarAction.Account { navigator.navigate(directionProvider.fromScreen(Screen.Account)) }
        )
    }

    topBarState.customTopBar(
        searchState = rememberSearchState(),
        actions = actions.toImmutableList()
    ) {
        SetupTopBar(homeUI = (state as? HomeUIState.Loaded)?.homeUI)
    }
    HomeScreenContent(state = state)
}

@Composable
private fun HomeScreenContent(
    state: HomeUIState
) {
    when (state) {
        is HomeUIState.Loaded -> HomeLoaded()
    }
}

@Composable
private fun HomeLoaded() {
    // Lets assume that the Icon + Text height will be 80 + 20
    val paddingBottom = (LocalConfiguration.current.screenHeightDp / 2) - SCREEN_CONTENT_HEIGHT
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingBottom.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(AlfieIcons.Home),
                contentDescription = null,
                modifier = Modifier.size(Theme.iconSize.xxLarge)
            )
            Text(
                text = "Home",
                style = Theme.typography.paragraphBold
            )
        }
    }
}

@Composable
private fun TopBarScope.SetupTopBar(homeUI: HomeUI?) {
    val type = if (homeUI?.userName != null) {
        LandingHeaderType.Greeting(
            userName = homeUI.userName,
            subtitle = homeUI.membershipDate
        )
    } else {
        LandingHeaderType.Logo()
    }

    LandingHeader(type = type)
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    Theme {
        val homeUI = HomeUI(
            userName = "User",
            membershipDate = "1982"
        )

        HomeScreenContent(
            HomeUIState.Loaded(homeUI)
        )
    }
}
