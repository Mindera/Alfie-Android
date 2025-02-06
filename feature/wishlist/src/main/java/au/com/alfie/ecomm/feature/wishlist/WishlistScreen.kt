package au.com.alfie.ecomm.feature.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.wishlist.WishlistNavArgs
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCard
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.action.TopBarAction
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.wishlist.models.WishlistProductUi
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
    val state by viewModel.state.collectAsStateWithLifecycle()

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
    WishlistScreenContent(state)
}

@Composable
private fun WishlistScreenContent(state: WishlistUiState) {
    when (state) {
        is WishlistUiState.Data.Loading -> EmptyBagScreen()
        is WishlistUiState.Data.Loaded -> {
            if (state.wishlist.isEmpty()) {
                EmptyBagScreen()
            } else {
                WishlistScreen(state.wishlist)
            }
        }

        else -> EmptyBagScreen()
    }
}

@Composable
private fun WishlistScreen(
    wishlist: List<WishlistProductUi>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // TODO consider using some sort of calculation to define the number of columns based on the screen size
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(wishlist) { item ->
            ProductCard(
                productCardType = item.productCardData,
                onClick = { },
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
        WishlistScreenContent(
            WishlistUiState.Data.Loading
        )
    }
}
