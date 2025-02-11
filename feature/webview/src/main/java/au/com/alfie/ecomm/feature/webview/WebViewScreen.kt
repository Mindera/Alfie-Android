package au.com.alfie.ecomm.feature.webview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.webview.WebViewNavArgs
import au.com.alfie.ecomm.core.navigation.arguments.wishlist.wishlistNavArgs
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.util.stringResource
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.dialog.error.ErrorType
import au.com.alfie.ecomm.designsystem.component.searchbar.rememberSearchState
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomHostState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.action.TopBarAction
import au.com.alfie.ecomm.feature.R
import au.com.alfie.ecomm.feature.uievent.handleUIEvents
import au.com.alfie.ecomm.feature.webview.model.WebViewUIState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.toImmutableList

@Destination(navArgsDelegate = WebViewNavArgs::class)
@Composable
fun WebViewScreen(
    destinationsNavigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel: WebViewViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val searchState = rememberSearchState()
    val actions = remember {
        buildList {
            if (viewModel.hasSearchAction) add(TopBarAction.Search(searchState))
            if (viewModel.hasWishlistAction) add(TopBarAction.WishList { destinationsNavigator.navigate(directionProvider.fromScreen(Screen.Wishlist(args = wishlistNavArgs()))) })
            if (viewModel.hasAccountAction) add(TopBarAction.Account { destinationsNavigator.navigate(directionProvider.fromScreen(Screen.Account)) })
        }
    }

    if (viewModel.topBarTitle != null) {
        topBarState.textTopBar(
            title = stringResource(resource = viewModel.topBarTitle),
            showNavigationIcon = viewModel.isBottomBarItem.not(),
            isLeftAligned = viewModel.isTitleLeftAligned,
            actions = actions.toImmutableList()
        )
    } else {
        topBarState.logoTopBar(showNavigationIcon = true)
    }

    if (viewModel.isBottomBarItem) {
        bottomBarState.showBottomBar()
    } else {
        bottomBarState.hideBottomBar()
    }

    viewModel.handleUIEvents(
        navigator = destinationsNavigator,
        navController = navController,
        directionProvider = directionProvider,
        snackbarHostState = snackbarHostState
    )

    WebViewScreenContent(
        state = state,
        deeplinkHandler = viewModel.deeplinkHandler,
        onEvent = { viewModel.run { handleWebViewEvent(it) } }
    )
}

@Composable
private fun WebViewScreenContent(
    state: WebViewUIState,
    deeplinkHandler: DeeplinkHandler,
    onEvent: ClickEventOneArg<WebViewEvent>
) {
    when (state) {
        is WebViewUIState.Loaded -> WebViewContent(
            url = state.content.url,
            queryParameters = state.content.queryParameters,
            headers = state.content.headers,
            deeplinkHandler = deeplinkHandler,
            onEvent = onEvent,
            errorType = ErrorType(
                message = androidx.compose.ui.res.stringResource(R.string.error_failed_to_load_page),
                buttonLabel = androidx.compose.ui.res.stringResource(R.string.retry)
            )
        )

        is WebViewUIState.Error -> WebViewError()
        else -> Unit
    }
}

@Composable
private fun WebViewError() {
    // TODO Update Error page when available
    Text(text = "Error loading WebView")
}
