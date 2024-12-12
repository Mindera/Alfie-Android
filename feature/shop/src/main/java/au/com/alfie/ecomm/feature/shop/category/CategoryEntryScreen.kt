package au.com.alfie.ecomm.feature.shop.category

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.navigation.arguments.CategoryNavArgs
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.util.stringResource
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomHostState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEntryUI
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEvent
import au.com.alfie.ecomm.feature.uievent.handleUIEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Destination(navArgsDelegate = CategoryNavArgs::class)
@Composable
internal fun ShopCategoryScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel: CategoryEntryViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    bottomBarState.showBottomBar()

    viewModel.handleUIEvents(
        navigator = navigator,
        navController = navController,
        directionProvider = directionProvider,
        snackbarHostState = snackbarHostState
    )

    topBarState.textTopBar(
        title = stringResource(resource = state.title),
        showNavigationIcon = true,
        isLeftAligned = false
    )

    CategoryScreenContent(
        entries = state.entries,
        onEvent = viewModel::handleEvent
    )
}

@Composable
private fun CategoryScreenContent(
    entries: ImmutableList<CategoryEntryUI>,
    onEvent: ClickEventOneArg<CategoryEvent>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        categoryItems(
            entries = entries,
            isPlaceholder = false,
            onEntryClick = { onEvent(CategoryEvent.OnEntryClickEvent(it)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShopScreenPreview() {
    Theme {
        CategoryScreenContent(
            entries = persistentListOf(
                CategoryEntryUI(
                    id = 1,
                    title = StringResource.fromText("New in"),
                    path = "url"
                ),
                CategoryEntryUI(
                    id = 2,
                    title = StringResource.fromText("Sale"),
                    path = "url"
                )
            ),
            onEvent = {}
        )
    }
}
