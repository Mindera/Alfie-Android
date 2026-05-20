package com.mindera.alfie.feature.bag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.wishlist.wishlistNavArgs
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.productcard.ProductCard
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomHostState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.action.TopBarAction
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.theme.dimen.Spacing.spacing10
import com.mindera.alfie.feature.bag.models.BagProductUi
import com.mindera.alfie.feature.uievent.handleUIEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.mindera.alfie.designsystem.R as RD

@Destination
@Composable
internal fun BagScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel: BagViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val actions = persistentListOf(
        TopBarAction.WishList { navigator.navigate(directionProvider.fromScreen(Screen.Wishlist(args = wishlistNavArgs()))) },
        TopBarAction.Account { navigator.navigate(directionProvider.fromScreen(Screen.Account)) }
    )

    topBarState.textTopBar(
        title = stringResource(R.string.bag_screen_title),
        showNavigationIcon = false,
        isLeftAligned = true,
        actions = actions
    )
    bottomBarState.showBottomBar()

    viewModel.handleUIEvents(
        navigator = navigator,
        navController = navController,
        directionProvider = directionProvider,
        snackbarHostState = snackbarHostState
    )

    BagScreenContent(state)
}

@Composable
private fun BagScreenContent(
    state: BagUiState
) {
    when (state) {
        is BagUiState.Data.Loaded -> BagListScreen(bag = state.bag)
        is BagUiState.Data.Empty,
        is BagUiState.Data.Loading,
        is BagUiState.Error -> EmptyBagScreen()
    }
}

@Composable
private fun BagListScreen(
    bag: ImmutableList<BagProductUi>
) {
    LazyColumn(
        contentPadding = PaddingValues(spacing10),
        verticalArrangement = Arrangement.spacedBy(spacing10)
    ) {
        items(bag) { item ->
            ProductCard(
                productCardType = item.productCardData
            )
        }
    }
}

@Composable
private fun EmptyBagScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(RD.drawable.ic_action_bag),
                contentDescription = null,
                modifier = Modifier.size(Theme.iconSize.xxLarge)
            )
            Text(
                text = "Bag",
                style = Theme.typography.paragraphBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BagScreenPreview() {
    Theme {
        BagScreenContent(BagUiState.Data.Loading)
    }
}
