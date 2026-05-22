package com.mindera.alfie.feature.shop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.deeplink.DeeplinkHandler
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.navigation.arguments.shop.ShopNavArgs
import com.mindera.alfie.core.navigation.arguments.shop.ShopTab
import com.mindera.alfie.core.navigation.arguments.wishlist.wishlistNavArgs
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.core.ui.test.SERVICES_PAGE
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.dialog.error.ErrorType
import com.mindera.alfie.designsystem.component.segmented.SegmentedItem
import com.mindera.alfie.designsystem.component.segmented.SegmentedPage
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomHostState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.action.TopBarAction
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.feature.shop.brand.ShopBrandsScreen
import com.mindera.alfie.feature.shop.category.ShopCategoriesScreen
import com.mindera.alfie.feature.shop.model.ShopUI
import com.mindera.alfie.feature.shop.model.ShopUIState
import com.mindera.alfie.feature.shop.ui.ShopErrorScreen
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.handleUIEvent
import com.mindera.alfie.feature.uievent.handleUIEvents
import com.mindera.alfie.feature.webview.WebViewContent
import com.mindera.alfie.feature.webview.WebViewEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import com.mindera.alfie.feature.R as FeatureR

@Destination(navArgsDelegate = ShopNavArgs::class)
@Composable
internal fun ShopScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    topBarState: TopBarState,
    bottomBarState: BottomBarState,
    shopNavArgs: ShopNavArgs
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
            ShopScreenContent(
                state = state as ShopUIState.Data,
                initialTab = shopNavArgs.tab,
                onUiEvent = { uiEvent ->
                    coroutineScope.launch {
                        uiEvent.handleUIEvent(
                            navigator = navigator,
                            navController = navController,
                            directionProvider = directionProvider,
                            snackbarHostState = snackbarHostState
                        )
                    }
                },
                deeplinkHandler = viewModel.deeplinkHandler,
                onWebViewEvent = { viewModel.run { handleWebViewEvent(it) } }
            )
        }
        is ShopUIState.Error -> {
            ShopErrorScreen(
                errorType = (state as ShopUIState.Error).errorType,
                onRetry = {}
            )
        }
    }
}

@Composable
private fun ShopScreenContent(
    state: ShopUIState.Data,
    initialTab: ShopTab,
    onUiEvent: ClickEventOneArg<UIEvent>,
    deeplinkHandler: DeeplinkHandler,
    onWebViewEvent: ClickEventOneArg<WebViewEvent>
) {
    var selectedSegment by rememberSaveable { mutableIntStateOf(ShopTab.entries.indexOf(initialTab)) }
    val segments: ImmutableList<Pair<SegmentedItem, @Composable () -> Unit>> = persistentListOf(
        SegmentedItem(label = StringResource.fromId(R.string.shop_segment_categories)) to {
            ShopCategoriesScreen(
                onUiEvent = onUiEvent
            )
        },
        SegmentedItem(label = StringResource.fromId(R.string.shop_segment_brands)) to {
            ShopBrandsScreen(
                onUiEvent = onUiEvent
            )
        },
        SegmentedItem(label = StringResource.fromId(R.string.shop_segment_services)) to {
            WebViewContent(
                url = (state as? ShopUIState.Data)?.shopUI?.servicesUrl.orEmpty(),
                queryParameters = emptyMap(),
                headers = emptyMap(),
                deeplinkHandler = deeplinkHandler,
                onEvent = onWebViewEvent,
                isBackHandlerEnabled = false,
                modifier = Modifier.testTag(SERVICES_PAGE),
                errorType = ErrorType(
                    message = stringResource(FeatureR.string.error_failed_to_load_page),
                    buttonLabel = stringResource(FeatureR.string.retry)
                )
            )
        }
    )

    SegmentedPage(
        segments = segments.map { it.first }.toImmutableList(),
        selectedSegment = selectedSegment,
        onSegmentClick = { selectedSegment = it },
        modifier = Modifier.fillMaxSize()
    ) { page ->
        segments[page].second()
    }
}

@Preview(showBackground = true)
@Composable
private fun ShopScreenPreview() {
    Theme {
        ShopScreenContent(
            state = ShopUIState.Data(
                shopUI = ShopUI(
                    servicesUrl = "servicesUrl"
                )
            ),
            initialTab = ShopTab.Categories,
            onUiEvent = { },
            deeplinkHandler = DeeplinkHandler(emptySet()),
            onWebViewEvent = { }
        )
    }
}
