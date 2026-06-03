package com.mindera.alfie.feature.plp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.navigation.arguments.productlist.ProductListNavArgs
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.core.ui.extension.handleWindowType
import com.mindera.alfie.core.ui.extension.itemsIndexed
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.designsystem.animation.standard
import com.mindera.alfie.designsystem.component.bottombar.BottomBarState
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.component.chip.Chip
import com.mindera.alfie.designsystem.component.loading.LoadingType
import com.mindera.alfie.designsystem.component.loading.LoadingWithLabel
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCard
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.designsystem.component.snackbar.SnackbarCustomHostState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.feature.plp.filter.RefineSheet
import com.mindera.alfie.feature.plp.model.ProductListEntryUI
import com.mindera.alfie.feature.plp.model.ProductListEvent
import com.mindera.alfie.feature.plp.model.ProductListUI
import com.mindera.alfie.feature.plp.model.QuickFilterChipUI
import com.mindera.alfie.feature.uievent.handleUIEvents
import com.mindera.alfie.repository.productlist.model.ProductListLayoutMode
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.persistentListOf
import com.mindera.alfie.designsystem.R as RD

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
        title = viewModel.collectionTitle,
        actions = persistentListOf()
    )
    bottomBarState.hideBottomBar()

    LaunchedEffect(Unit) {
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

        if (state.showRefine) {
            RefineSheet(
                currentSort = state.selectedSort,
                currentFilters = state.selectedFilters,
                totalCount = state.resultCount,
                onApply = { sort, filters ->
                    onEvent(ProductListEvent.ApplySort(sort))
                    onEvent(ProductListEvent.ApplyFilters(filters))
                    onEvent(ProductListEvent.DismissRefine)
                },
                onDismiss = { onEvent(ProductListEvent.DismissRefine) }
            )
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
                onEvent = onEvent
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columnCount),
                horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16)
            ) {
                item(span = { GridItemSpan(columnCount) }) {
                    ToolbarSection(
                        resultCount = state.resultCount,
                        layoutMode = state.layoutMode,
                        onEvent = onEvent,
                        isLoading = state.isLoadingMetadata
                    )
                }
                // TODO: ALFMOB-337 – Add a horizontally-scrollable quick-filter chip row here once
                //  the BFF exposes available filter facets (e.g. brand names, product types, sizes).
                //  Each chip should toggle the corresponding filter on/off without opening the Refine
                //  sheet. BFF schema currently has no facets in ProductListResponse.
                if (state.availableFilters.isNotEmpty()) {
                    item(span = { GridItemSpan(columnCount) }) {
                        FilterChipsRow(
                            filters = state.availableFilters,
                            onToggle = { chipId -> onEvent(ProductListEvent.ToggleFilterChip(chipId)) }
                        )
                    }
                }
                itemsIndexed(
                    items = products,
                    key = { it.id }
                ) { index, item ->
                    item?.let { entry ->
                        ProductCard(
                            productCardType = entry.productCardData,
                            modifier = Modifier.productListEntryPadding(
                                index = index,
                                columnCount = columnCount
                            ),
                            isWishlisted = state.wishlistIds.contains(entry.id)
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
    onEvent: ClickEventOneArg<ProductListEvent>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16)
    ) {
        item(span = { GridItemSpan(columnCount) }) {
            ToolbarSection(
                resultCount = state.resultCount,
                layoutMode = state.layoutMode,
                onEvent = onEvent,
                isLoading = state.isLoadingMetadata
            )
        }
        items(count = NUM_LOADING_ITEMS) { index ->
            ProductListGridLoadingItem(
                index = index,
                columnCount = columnCount
            )
        }
    }
}

@Composable
private fun ToolbarSection(
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
                horizontal = Theme.spacing.spacing12,
                vertical = Theme.spacing.spacing8
            )
    ) {
        LayoutModeToggle(
            layoutMode = layoutMode,
            onEvent = onEvent
        )
        ResultCounter(
            resultCount = resultCount,
            isLoading = isLoading
        )
        RefineButton(
            onClick = { onEvent(ProductListEvent.OpenFilters) },
            isLoading = isLoading
        )
    }
}

@Composable
private fun RefineButton(
    onClick: ClickEvent,
    isLoading: Boolean
) {
    Button(
        type = ButtonType.Underlined,
        text = stringResource(R.string.filters_button_label),
        onClick = onClick,
        buttonSize = ButtonSize.Small,
        isEnabled = isLoading.not()
    )
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
private fun FilterChipsRow(
    filters: List<QuickFilterChipUI>,
    onToggle: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = Theme.spacing.spacing12),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing8),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(filters, key = { it.id }) { chip ->
            Chip(
                label = chip.label,
                isSelected = chip.isSelected,
                onClickEvent = { onToggle(chip.id) }
            )
        }
    }
}

@Composable
private fun LayoutModeToggle(
    layoutMode: ProductListLayoutMode,
    onEvent: ClickEventOneArg<ProductListEvent>
) {
    Row {
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
    columnCount: Int
) {
    ProductCard(
        productCardType = ProductCardType.Vertical(
            image = ImageUI(images = persistentListOf(), alt = null),
            brand = "",
            name = "",
            price = PriceType.Default(""),
            onFavoriteClick = { }
        ),
        modifier = Modifier.productListEntryPadding(
            index = index,
            columnCount = columnCount
        ),
        isLoading = true
    )
}
