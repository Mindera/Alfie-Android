package com.mindera.alfie.feature.shop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.shop.ShopNavArgs
import com.mindera.alfie.core.navigation.arguments.wishlist.wishlistNavArgs
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomHostState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.action.TopBarAction
import com.mindera.alfie.feature.shop.category.ShopCategoriesScreen
import com.mindera.alfie.feature.shop.model.ShopUIState
import com.mindera.alfie.feature.shop.ui.ShopErrorScreen
import com.mindera.alfie.feature.uievent.handleUIEvent
import com.mindera.alfie.feature.uievent.handleUIEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Destination(navArgsDelegate = ShopNavArgs::class)
@Composable
internal fun ShopScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel: ShopViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val actions = persistentListOf(
        TopBarAction.WishList { navigator.navigate(directionProvider.fromScreen(Screen.Wishlist(args = wishlistNavArgs()))) },
        TopBarAction.Account { navigator.navigate(directionProvider.fromScreen(Screen.Account)) }
    )

    topBarState.textTopBar(
        title = stringResource(id = R.string.shop_screen_title),
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

    when (state) {
        is ShopUIState.Data -> {
            ShopCategoriesScreen(
                onUiEvent = { uiEvent ->
                    coroutineScope.launch {
                        uiEvent.handleUIEvent(
                            navigator = navigator,
                            navController = navController,
                            directionProvider = directionProvider,
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            )
        }
        is ShopUIState.Error -> {
            ShopErrorScreen(
                errorType = (state as ShopUIState.Error).errorType
            )
        }
    }
}
