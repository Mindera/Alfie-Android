package com.mindera.alfie.feature.shop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.navigation.arguments.shop.ShopNavArgs
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.searchbar.rememberSearchState
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomHostState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.custom.SearchHeader
import com.mindera.alfie.feature.shop.category.ShopCategoriesScreen
import com.mindera.alfie.feature.uievent.handleUIEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

/**
 * Shop tab root. It owns the chrome only — the category list holds its own state, including its
 * loading and error surfaces, so this screen has no view model of its own.
 *
 * The chrome is the Figma "Menu Sheet - Level 1" (node `671:78533`): the search entry point sits
 * alone at the top, with no title row and no top-bar actions — Wishlist and Account are reached
 * from the bottom bar. Sub-categories (levels 2 and 3) are the ones that carry a titled header.
 */
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
    val coroutineScope = rememberCoroutineScope()
    val searchState = rememberSearchState()

    topBarState.customTopBar(searchState = searchState) {
        SearchHeader(searchState = searchState)
    }
    bottomBarState.showBottomBar()

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
