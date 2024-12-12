package au.com.alfie.ecomm.feature.home

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
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.debug.runner.LocalDebugComposeRunner
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.searchbar.rememberSearchState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.action.TopBarAction
import au.com.alfie.ecomm.designsystem.component.topbar.custom.LandingHeader
import au.com.alfie.ecomm.designsystem.component.topbar.custom.LandingHeaderType
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScope
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.home.model.HomeUI
import au.com.alfie.ecomm.feature.home.model.HomeUIState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.toImmutableList

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
                painter = painterResource(R.drawable.ic_action_house),
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
