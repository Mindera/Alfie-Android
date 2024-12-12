package au.com.alfie.ecomm.feature.plp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.navigation.arguments.productlist.ProductListNavArgs
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.extension.handleWindowType
import au.com.alfie.ecomm.core.ui.extension.itemsIndexed
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.animation.standard
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.loading.LoadingType
import au.com.alfie.ecomm.designsystem.component.loading.LoadingWithLabel
import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCard
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.designsystem.component.searchbar.rememberSearchState
import au.com.alfie.ecomm.designsystem.component.snackbar.SnackbarCustomHostState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.action.TopBarAction
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.plp.model.ProductListEntryUI
import au.com.alfie.ecomm.feature.plp.model.ProductListEvent
import au.com.alfie.ecomm.feature.plp.model.ProductListUI
import au.com.alfie.ecomm.feature.uievent.handleUIEvents
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.persistentListOf
import au.com.alfie.ecomm.designsystem.R as RD

private const val NUM_LOADING_ITEMS = 16

@Destination(navArgsDelegate = ProductListNavArgs::class)
@Composable
internal fun ProductListScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    directionProvider: DirectionProvider,
    snackbarHostState: SnackbarCustomHostState,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    val viewModel: ProductListViewModel = hiltViewModel()
    val products = viewModel.productPager.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()

    topBarState.textTopBar(
        title = state.title,
        actions = persistentListOf(TopBarAction.Search(rememberSearchState()))
    )
    bottomBarState.hideBottomBar()

    LaunchedEffect(Unit) {
        // Check if the layout mode was changed on back navigation
        if (state != ProductListUI.EMPTY) {
            viewModel.checkLayoutModePreference()
        }
    }

    viewModel.handleUIEvents(
        navigator = navigator,
        navController = navController,
        directionProvider = directionProvider,
        snackbarHostState = snackbarHostState
    )

    ProductListScreenContent(
        state = state,
        products = products,
        onEvent = viewModel::handleEvent
    )
}

@Composable
private fun ProductListScreenContent(
    state: ProductListUI,
    products: LazyPagingItems<ProductListEntryUI>,
    onEvent: ClickEventOneArg<ProductListEvent>
) {
    val isError = products.loadState.refresh is LoadState.Error
    val isEmpty = products.loadState.refresh is LoadState.NotLoading && products.itemCount == 0

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isError -> { /* TODO Error */ }
            isEmpty -> { /* TODO Empty */ }
            else -> {
                ProductListGrid(
                    state = state,
                    products = products,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun ProductListGrid(
    state: ProductListUI,
    products: LazyPagingItems<ProductListEntryUI>,
    onEvent: ClickEventOneArg<ProductListEvent>
) {
    if (state == ProductListUI.EMPTY) return
    val isLoading = products.loadState.refresh is LoadState.Loading

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val columnCount = handleWindowType(
            onCompactWindow = { state.compactColumnCount },
            onNotCompactWindow = { state.nonCompactColumnCount }
        )

        if (isLoading) {
            ProductListLoadingState(
                state = state,
                columnCount = columnCount,
                layoutMode = state.layoutMode,
                onEvent = onEvent
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columnCount),
                horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16)
            ) {
                item(span = { GridItemSpan(columnCount) }) {
                    ResultCounterSection(
                        resultCount = state.resultCount,
                        layoutMode = state.layoutMode,
                        onEvent = onEvent,
                        isLoading = state.isLoadingMetadata
                    )
                }
                itemsIndexed(
                    items = products,
                    key = { it.id }
                ) { index, item ->
                    item?.let { entry ->
                        ProductCard(
                            productCardType = entry.productCardData,
                            onClick = { onEvent(ProductListEvent.OpenProduct(entry.id)) },
                            modifier = Modifier.productListEntryPadding(
                                index = index,
                                columnCount = columnCount
                            )
                        )
                    }
                }
                item(span = { GridItemSpan(columnCount) }) {
                    AnimatedVisibility(
                        visible = products.loadState.append is LoadState.Loading,
                        enter = expandVertically(),
                        exit = shrinkVertically(shrinkTowards = Alignment.Top)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LoadingWithLabel(
                                type = LoadingType.Dark,
                                modifier = Modifier.padding(vertical = Theme.spacing.spacing32)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductListLoadingState(
    state: ProductListUI,
    columnCount: Int,
    layoutMode: ProductListLayoutMode,
    onEvent: ClickEventOneArg<ProductListEvent>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16)
    ) {
        item(span = { GridItemSpan(columnCount) }) {
            ResultCounterSection(
                resultCount = state.resultCount,
                layoutMode = state.layoutMode,
                onEvent = onEvent,
                isLoading = state.isLoadingMetadata
            )
        }
        items(count = NUM_LOADING_ITEMS) { index ->
            ProductListGridLoadingItem(
                index = index,
                columnCount = columnCount,
                layoutMode = layoutMode
            )
        }
    }
}

@Composable
private fun ResultCounterSection(
    resultCount: Int,
    layoutMode: ProductListLayoutMode,
    onEvent: ClickEventOneArg<ProductListEvent>,
    isLoading: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Theme.spacing.spacing12,
                top = Theme.spacing.spacing8,
                bottom = Theme.spacing.spacing8
            )
    ) {
        FiltersButton(
            onClick = { onEvent(ProductListEvent.OpenFilters) },
            isLoading = isLoading
        )
        ResultCounter(
            resultCount = resultCount,
            isLoading = isLoading
        )
        LayoutModeToggle(
            layoutMode = layoutMode,
            onEvent = onEvent
        )
    }
}

@Composable
private fun FiltersButton(
    onClick: ClickEvent,
    isLoading: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = Theme.shape.extraSmall)
            .heightIn(44.dp)
            .clickable(
                onClick = onClick,
                enabled = isLoading.not(),
                role = Role.Button
            )
    ) {
        Icon(
            modifier = Modifier
                .size(Theme.iconSize.small)
                .padding(start = Theme.spacing.spacing4),
            painter = painterResource(id = RD.drawable.ic_action_filters),
            contentDescription = null,
            tint = Theme.color.primary.mono900
        )
        Spacer(modifier = Modifier.width(Theme.spacing.spacing8))
        Text(
            modifier = Modifier.padding(end = Theme.spacing.spacing4),
            text = stringResource(R.string.filters_button_label),
            style = Theme.typography.paragraph,
            color = Theme.color.primary.mono900
        )
    }
}

@Composable
private fun ResultCounter(
    resultCount: Int,
    isLoading: Boolean
) {
    AnimatedVisibility(
        visible = isLoading.not(),
        enter = fadeIn(standard()),
        exit = fadeOut(standard())
    ) {
        Text(
            text = stringResource(id = R.string.results_counter, resultCount),
            style = Theme.typography.tiny,
            color = Theme.color.primary.mono500
        )
    }
}

@Composable
private fun LayoutModeToggle(
    layoutMode: ProductListLayoutMode,
    onEvent: ClickEventOneArg<ProductListEvent>
) {
    Row(modifier = Modifier.padding(start = Theme.spacing.spacing4)) {
        LayoutModeButton(
            layoutMode = ProductListLayoutMode.GRID,
            selectedLayoutMode = layoutMode,
            onEvent = onEvent
        )
        LayoutModeButton(
            layoutMode = ProductListLayoutMode.COLUMN,
            selectedLayoutMode = layoutMode,
            onEvent = onEvent
        )
    }
}

@Composable
private fun LayoutModeButton(
    layoutMode: ProductListLayoutMode,
    selectedLayoutMode: ProductListLayoutMode,
    onEvent: ClickEventOneArg<ProductListEvent>
) {
    val icon = when (layoutMode) {
        ProductListLayoutMode.GRID -> RD.drawable.ic_informational_grid
        ProductListLayoutMode.COLUMN -> RD.drawable.ic_informational_column
    }
    val (isEnabled, color) = if (layoutMode == selectedLayoutMode) {
        false to Theme.color.primary.mono900
    } else {
        true to Theme.color.primary.mono200
    }

    IconButton(
        enabled = isEnabled,
        onClick = { onEvent(ProductListEvent.ChangeLayoutMode(layoutMode)) },
        modifier = Modifier.size(Theme.iconSize.xLarge)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(Theme.iconSize.small)
        )
    }
}

@Composable
private fun Modifier.productListEntryPadding(
    index: Int,
    columnCount: Int
): Modifier {
    val startPadding = if (index % columnCount == 0) Theme.spacing.spacing16 else Theme.spacing.spacing0
    val endPadding = if (index % columnCount == columnCount - 1) Theme.spacing.spacing16 else Theme.spacing.spacing0

    return this.padding(
        start = startPadding,
        end = endPadding,
        top = Theme.spacing.spacing8,
        bottom = Theme.spacing.spacing8
    )
}

@Composable
private fun ProductListGridLoadingItem(
    index: Int,
    columnCount: Int,
    layoutMode: ProductListLayoutMode
) {
    val type = when (layoutMode) {
        ProductListLayoutMode.GRID -> ProductCardType.Medium(
            image = ImageUI(images = persistentListOf(), alt = null),
            brand = "",
            name = "",
            price = PriceType.Default(""),
            onFavoriteClick = { }
        )
        ProductListLayoutMode.COLUMN -> ProductCardType.Large(
            image = ImageUI(images = persistentListOf(), alt = null),
            brand = "",
            name = "",
            price = PriceType.Default(""),
            onFavoriteClick = { }
        )
    }

    ProductCard(
        productCardType = type,
        modifier = Modifier.productListEntryPadding(
            index = index,
            columnCount = columnCount
        ),
        onClick = { },
        isLoading = true
    )
}
