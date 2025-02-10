package au.com.alfie.ecomm.feature.bag

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.wishlist.wishlistNavArgs
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCard
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.action.TopBarAction
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.designsystem.theme.dimen.Spacing.spacing10
import au.com.alfie.ecomm.feature.bag.models.BagProductUi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.persistentListOf
import au.com.alfie.ecomm.designsystem.R as RD

@Destination
@Composable
internal fun BagScreen(
    navigator: DestinationsNavigator,
    directionProvider: DirectionProvider,
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

    BagScreenContent(state)
}

@Composable
private fun BagScreenContent(
    state: BagUiState
) {
    when (state) {
        is BagUiState.Data.Loading -> EmptyBagScreen()
        is BagUiState.Data.Loaded -> {
            if (state.bag.isEmpty()) {
                EmptyBagScreen()
            } else {
                BagListScreen(state.bag)
            }
        }

        else -> EmptyBagScreen()
    }
}

@Composable
private fun BagListScreen(
    bag: List<BagProductUi>
) {
    LazyColumn(
        contentPadding = PaddingValues(spacing10),
        verticalArrangement = Arrangement.spacedBy(spacing10)
    ) {
        items(bag) { item ->
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
