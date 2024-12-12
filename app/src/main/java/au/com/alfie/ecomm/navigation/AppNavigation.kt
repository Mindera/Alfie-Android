package au.com.alfie.ecomm.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.navigation.arguments.webview.webViewNavArgs
import au.com.alfie.ecomm.designsystem.animation.standard
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBar
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.bottombar.rememberBottomBarState
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomHost
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomHostState
import au.com.alfie.ecomm.designsystem.component.snackbar.rememberSnackbarCustomHostState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBar
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.component.topbar.rememberTopBarState
import au.com.alfie.ecomm.feature.home.destinations.HomeScreenDestination
import au.com.alfie.ecomm.feature.search.SearchOverlay
import au.com.alfie.ecomm.feature.webview.destinations.WebViewScreenDestination
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.utils.currentDestinationAsState

@OptIn(
    ExperimentalMaterialNavigationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun AppNavigation(
    startDestination: Screen,
    navGraphs: NavGraphs,
    directionProvider: DirectionProvider,
    deeplinkHandler: DeeplinkHandler,
    wishlistToggleEnabled: Boolean
) {
    val navController = rememberNavController()
    val topBarState = rememberTopBarState(isVisible = false)
    val bottomBarState = rememberBottomBarState()
    val snackbarHostState = rememberSnackbarCustomHostState()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    navController.navigatorProvider += bottomSheetNavigator

    LaunchedEffect(Unit) {
        deeplinkHandler.deeplinkResult.collect { result ->
            when (result) {
                is DeeplinkResult.NavigateTo -> {
                    navController.navigate(
                        direction = result.direction,
                        navOptionsBuilder = result.navOptions
                    )
                }

                is DeeplinkResult.NavigateClearingStack -> {
                    navController.popBackStack(
                        destinationId = navController.graph.findStartDestination().id,
                        inclusive = result.clearStartDestination
                    )
                    navController.navigate(
                        direction = result.direction,
                        navOptionsBuilder = {
                            launchSingleTop = result.launchSingleTop
                            restoreState = result.restoreState
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = result.clearStartDestination
                                saveState = result.saveState
                            }
                        }
                    )
                }

                is DeeplinkResult.Unresolved -> {
                    navController.navigate(
                        direction = WebViewScreenDestination(webViewNavArgs(url = result.url))
                    )
                }
            }
        }
    }

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {
                TopBar(
                    state = topBarState,
                    onNavigationClick = { navController.navigateUp() }
                )
            },
            snackbarHost = { SnackbarCustomHost(snackbarHostState) },
            modifier = Modifier
                .fillMaxSize()
                .semantics { testTagsAsResourceId = true }
                .systemBarsPadding()
        ) { padding ->
            (topBarState.title as? TopBarTitle.Search)?.customOverlay?.let { content ->
                content(padding) { innerPadding ->
                    BottomBarScaffold(
                        paddingValues = innerPadding,
                        startDestination = startDestination,
                        navGraphs = navGraphs,
                        topBarState = topBarState,
                        bottomBarState = bottomBarState,
                        snackbarCustomHostState = snackbarHostState,
                        getNavController = { navController },
                        directionProvider = directionProvider,
                        wishlistToggleEnabled = wishlistToggleEnabled
                    )
                }
            } ?: run {
                DefaultOverlayContent(
                    paddingValues = padding,
                    startDestination = startDestination,
                    navGraphs = navGraphs,
                    topBarState = topBarState,
                    bottomBarState = bottomBarState,
                    snackbarCustomHostState = snackbarHostState,
                    getNavController = { navController },
                    directionProvider = directionProvider,
                    wishlistToggleEnabled = wishlistToggleEnabled
                )
            }
        }
    }
}

@Composable
private fun DefaultOverlayContent(
    paddingValues: PaddingValues,
    startDestination: Screen,
    navGraphs: NavGraphs,
    topBarState: TopBarState,
    bottomBarState: BottomBarState,
    snackbarCustomHostState: SnackbarCustomHostState,
    getNavController: () -> NavHostController,
    directionProvider: DirectionProvider,
    wishlistToggleEnabled: Boolean
) {
    val isSearchOpen = topBarState.isSearchOpen()
    val searchState = topBarState.getSearchState()

    SearchOverlay(
        isOpen = isSearchOpen,
        onSearchAction = { handler ->
            searchState?.setOnSearchAction(handler)
        },
        onUpdateSearchTerm = { handler ->
            searchState?.setCustomOnSearchTermChange(handler)
        },
        navController = getNavController(),
        directionProvider = directionProvider,
        modifier = Modifier.padding(paddingValues),
        onDismiss = { searchState?.invertSearchOpenState() },
        content = {
            BottomBarScaffold(
                paddingValues = paddingValues,
                startDestination = startDestination,
                navGraphs = navGraphs,
                topBarState = topBarState,
                bottomBarState = bottomBarState,
                snackbarCustomHostState = snackbarCustomHostState,
                getNavController = getNavController,
                directionProvider = directionProvider,
                wishlistToggleEnabled = wishlistToggleEnabled
            )
        }
    )
}

@Composable
private fun BottomBarScaffold(
    paddingValues: PaddingValues,
    startDestination: Screen,
    navGraphs: NavGraphs,
    topBarState: TopBarState,
    bottomBarState: BottomBarState,
    snackbarCustomHostState: SnackbarCustomHostState,
    getNavController: () -> NavHostController,
    directionProvider: DirectionProvider,
    wishlistToggleEnabled: Boolean
) {
    val bottomBarItems = bottomBarItems(wishlistToggleEnabled)
    val navController = getNavController()
    val currentDestination = navController.currentDestinationAsState().value
    bottomBarItems.updateSelectedState(currentDestination)

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            BottomBar(
                state = bottomBarState,
                items = bottomBarItems,
                onItemClick = { _, item ->
                    (item as? BottomBarDestination)?.let {
                        val keepState = item.shouldRestore(currentDestination)
                        navController.navigate(item.direction) {
                            launchSingleTop = true
                            restoreState = keepState
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = keepState
                            }
                        }
                    }
                }
            )
        },
        modifier = Modifier
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues)
            .systemBarsPadding()
    ) { innerPadding ->
        NavHostContent(
            paddingValues = innerPadding,
            startDestination = startDestination,
            navGraphs = navGraphs,
            topBarState = topBarState,
            bottomBarState = bottomBarState,
            snackbarCustomHostState = snackbarCustomHostState,
            getNavController = getNavController,
            directionProvider = directionProvider
        )
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Composable
private fun NavHostContent(
    paddingValues: PaddingValues,
    startDestination: Screen,
    navGraphs: NavGraphs,
    topBarState: TopBarState,
    bottomBarState: BottomBarState,
    snackbarCustomHostState: SnackbarCustomHostState,
    getNavController: () -> NavHostController,
    directionProvider: DirectionProvider
) {
    val startDirection = directionProvider.fromScreen(startDestination)
    val startRoute =
        navGraphs.root.destinationsByRoute.getOrDefault(startDirection.route, HomeScreenDestination)

    DestinationsNavHost(
        navGraph = navGraphs.root,
        navController = getNavController(),
        startRoute = startRoute,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        engine = rememberAnimatedNavHostEngine(
            rootDefaultAnimations = RootNavGraphDefaultAnimations(
                enterTransition = { fadeIn(animationSpec = standard()) },
                exitTransition = { fadeOut(animationSpec = standard()) }
            )
        ),
        dependenciesContainerBuilder = {
            dependency(directionProvider)
            dependency(topBarState)
            dependency(bottomBarState)
            dependency(snackbarCustomHostState)
        }
    )
}
