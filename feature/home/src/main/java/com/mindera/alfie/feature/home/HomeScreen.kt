package com.mindera.alfie.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.debug.runner.LocalDebugComposeRunner
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.highlights.Highlights
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
        is HomeUIState.Loaded -> HomeLoaded(homeUI = state.homeUI)
    }
}

@Composable
private fun HomeLoaded(homeUI: HomeUI) {
    // Hero-only for now — remaining Home sections land in a later ticket once the BFF is ready (ALFMOB-449).
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Highlights(items = homeUI.highlights)
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
        HomeScreenContent(
            HomeUIState.Loaded(HomeUIFactory().invoke())
        )
    }
}
