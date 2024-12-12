package au.com.alfie.ecomm.feature.wishlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.wishlist.WishlistNavArgs
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.action.TopBarAction
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.persistentListOf

@Destination(navArgsDelegate = WishlistNavArgs::class)
@Composable
internal fun WishlistScreen(
    navigator: DestinationsNavigator,
    directionProvider: DirectionProvider,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel: WishlistViewModel = hiltViewModel()
    if (viewModel.launchFromTop) {
        val actions = persistentListOf(
            TopBarAction.Account { navigator.navigate(directionProvider.fromScreen(Screen.Account)) }
        )
        topBarState.textTopBar(
            title = stringResource(id = R.string.wishlist_screen_title),
            showNavigationIcon = false,
            isLeftAligned = true,
            actions = actions
        )
        bottomBarState.showBottomBar()
    } else {
        topBarState.textTopBar(title = stringResource(id = R.string.wishlist_screen_title))
        bottomBarState.hideBottomBar()
    }
    WishlistScreenContent()
}

@Composable
private fun WishlistScreenContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_action_heart_outline),
                contentDescription = null,
                modifier = Modifier.size(Theme.iconSize.xxLarge)
            )
            Text(
                text = "Wishlist",
                style = Theme.typography.paragraphBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WishlistScreenPreview() {
    Theme {
        WishlistScreenContent()
    }
}
