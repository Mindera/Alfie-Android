package com.mindera.alfie.feature.plp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
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
import com.mindera.alfie.designsystem.component.state.StateMessage
import com.mindera.alfie.designsystem.component.state.StateMessageAction
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
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
import kotlinx.coroutines.flow.flowOf

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
    // D3: Figma renders the app-shell bottom bar as part of the PLP frame, with "Store" in the
    //  selected style — the listing keeps its tab context instead of hiding the bar.
    bottomBarState.showBottomBar()

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
        searchQuery = viewModel.searchQuery,
        onEvent = viewModel::handleEvent
    )
}

@Composable
private fun ProductListScreenContent(
    state: ProductListUI,
    products: LazyPagingItems<ProductListEntryUI>,
    searchQuery: String?,
    onEvent: ClickEventOneArg<ProductListEvent>
) {
    val isError = products.loadState.refresh is LoadState.Error
    val isEmpty = products.loadState.refresh is LoadState.NotLoading && products.itemCount == 0

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isError -> StateMessage(
                title = stringResource(id = R.string.plp_error_title),
                subtitle = stringResource(id = R.string.plp_error_subtitle),
                action = StateMessageAction(
                    label = stringResource(id = R.string.plp_error_retry),
                    onClick = { products.retry() }
                )
            )
            isEmpty && searchQuery != null -> StateMessage(
                title = stringResource(id = R.string.plp_search_no_results_title, searchQuery),
                subtitle = stringResource(id = R.string.plp_search_no_results_subtitle)
            )
            isEmpty -> StateMessage(
                title = stringResource(id = R.string.plp_empty_title),
                subtitle = stringResource(id = R.string.plp_empty_subtitle)
            )
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
                previewCount = state.previewResultCount,
                onPreviewFilters = { onEvent(ProductListEvent.PreviewRefine(it)) },
                onApply = { sort, filters ->
                    onEvent(ProductListEvent.ApplyRefine(sort, filters))
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
    val theme = LocalTheme.current
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
            // D-grid: outer margin (screen-size/margin = 16), column gutter (screen-size/gutter = 8)
            //  and row gap (16) belong to the grid itself rather than to each item.
            LazyVerticalGrid(
                columns = GridCells.Fixed(columnCount),
                contentPadding = PaddingValues(horizontal = theme.spacing.spacing16),
                horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8),
                verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing16)
            ) {
                item(span = { GridItemSpan(columnCount) }) {
                    RefineHeaderSection(
                        state = state,
                        onEvent = onEvent
                    )
                }
                itemsIndexed(
                    items = products,
                    key = { it.id }
                ) { _, item ->
                    item?.let { entry ->
                        ProductCard(
                            productCardType = entry.productCardData,
                            isWishlisted = state.wishlistIds.contains(entry.slug)
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
                                modifier = Modifier.padding(vertical = theme.spacing.spacing32)
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
    val theme = LocalTheme.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        contentPadding = PaddingValues(horizontal = theme.spacing.spacing16),
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing8),
        verticalArrangement = Arrangement.spacedBy(theme.spacing.spacing16)
    ) {
        item(span = { GridItemSpan(columnCount) }) {
            RefineHeaderSection(
                state = state,
                onEvent = onEvent
            )
        }
        items(count = NUM_LOADING_ITEMS) {
            ProductListGridLoadingItem()
        }
    }
}

/**
 * Toolbar row + quick-filter chips as a single span item, mirroring Figma's `Refine` container
 * (`I673:95608;1:3157`), which groups both rows and owns their shared 16 dp horizontal margin.
 * They must share one item because `LazyVerticalGrid` applies `verticalArrangement` uniformly to
 * span items, which would otherwise force a 16 dp gap where Figma wants 8.
 */
@Composable
private fun RefineHeaderSection(
    state: ProductListUI,
    onEvent: ClickEventOneArg<ProductListEvent>
) {
    Column(verticalArrangement = Arrangement.spacedBy(LocalTheme.current.spacing.spacing8)) {
        ToolbarSection(
            resultCount = state.resultCount,
            layoutMode = state.layoutMode,
            onEvent = onEvent,
            isLoading = state.isLoadingMetadata
        )
        // TODO: ALFMOB-337 – the quick-filter chip row is in the Figma design but stays hidden until
        //  the BFF exposes available filter facets (e.g. brand names, product types, sizes). Each chip
        //  should toggle the corresponding filter on/off without opening the Refine sheet. The BFF
        //  schema currently has no facets in ProductListResponse.
        if (state.availableFilters.isNotEmpty()) {
            FilterChipsRow(
                filters = state.availableFilters,
                onToggle = { chipId -> onEvent(ProductListEvent.ToggleFilterChip(chipId)) }
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
    // D-toolbar: 4 dp vertical padding only — the 16 dp horizontal margin comes from the grid's
    //  contentPadding, matching Figma where the `Refine` container owns the inset.
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = LocalTheme.current.spacing.spacing4)
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
    val theme = LocalTheme.current
    AnimatedVisibility(
        visible = isLoading.not(),
        enter = fadeIn(standard()),
        exit = fadeOut(standard())
    ) {
        // D-counter: body/small + content/content-terciary. Metrically identical to the previous
        //  label.small/neutrals500 pair (both 12/16 W400 #767676), but bound to the semantic layer.
        Text(
            text = pluralStringResource(id = R.plurals.results_counter, count = resultCount, resultCount),
            style = theme.typography.body.small,
            color = theme.color.content.contentTerciary
        )
    }
}

@Composable
private fun FilterChipsRow(
    filters: List<QuickFilterChipUI>,
    onToggle: (String) -> Unit
) {
    // D-chips: gap = screen-size/gutter (8). No content padding — the 16 dp margin is inherited
    //  from the grid's contentPadding.
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(LocalTheme.current.spacing.spacing8),
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
    val theme = LocalTheme.current
    val isSelected = layoutMode == selectedLayoutMode
    // D7: Figma expresses the toggle as two *different* glyphs — `Grid 1` vs `Grid 2 (Fill)` — so the
    //  selected mode uses the filled variant. Both states tint content/content-primary; the previous
    //  neutrals800-vs-neutrals200 tint difference is not in the design.
    val icon = when (layoutMode) {
        ProductListLayoutMode.GRID -> if (isSelected) AlfieIcons.Grid2Fill else AlfieIcons.Grid2
        ProductListLayoutMode.COLUMN -> if (isSelected) AlfieIcons.Grid1Fill else AlfieIcons.Grid1
    }

    IconButton(
        enabled = isSelected.not(),
        onClick = { onEvent(ProductListEvent.ChangeLayoutMode(layoutMode)) },
        modifier = Modifier.size(theme.sizing.icon.large)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            // Explicit tint overrides IconButton's disabled content colour, so the selected
            // (disabled) button still renders at full contentPrimary.
            tint = theme.color.content.contentPrimary,
            modifier = Modifier.size(theme.sizing.icon.medium)
        )
    }
}

@Composable
private fun ProductListGridLoadingItem() {
    ProductCard(
        productCardType = ProductCardType.Vertical(
            image = ImageUI(images = persistentListOf(), alt = null),
            brand = "",
            name = "",
            price = PriceType.Default(""),
            onFavoriteClick = { }
        ),
        isLoading = true
    )
}

// region Previews

private fun previewEntry(index: Int): ProductListEntryUI = ProductListEntryUI(
    id = "preview-$index",
    slug = "preview-$index",
    productCardData = ProductCardType.Vertical(
        image = ImageUI(images = persistentListOf(), alt = null),
        brand = "Brand Name",
        name = "100% Cotton Fluid Blazer",
        price = PriceType.Default("£170"),
        // Figma shows the promo label on the first card only (node I725:12955;3003:11344).
        label = "Best Seller".takeIf { index == 0 },
        onFavoriteClick = { }
    )
)

/**
 * Fakes a [LazyPagingItems] for previews. `PagingData.from(data, sourceLoadStates)` is public API
 * in paging 3.3.5, so terminal load states can be driven directly.
 */
@Composable
private fun previewProducts(
    items: List<ProductListEntryUI>,
    refresh: LoadState = LoadState.NotLoading(endOfPaginationReached = true)
): LazyPagingItems<ProductListEntryUI> = remember(items, refresh) {
    flowOf(
        PagingData.from(
            data = items,
            sourceLoadStates = LoadStates(
                refresh = refresh,
                prepend = LoadState.NotLoading(endOfPaginationReached = true),
                append = LoadState.NotLoading(endOfPaginationReached = true)
            )
        )
    )
}.collectAsLazyPagingItems()

private val previewGridState = ProductListUI.EMPTY.copy(
    resultCount = 14,
    isLoadingMetadata = false,
    layoutMode = ProductListLayoutMode.GRID,
    compactColumnCount = 2,
    nonCompactColumnCount = 3
)

@Preview(showBackground = true)
@Composable
private fun ProductListGridPreview() {
    Theme {
        ProductListScreenContent(
            state = previewGridState,
            products = previewProducts(items = List(size = 6) { previewEntry(index = it) }),
            searchQuery = null,
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductListColumnPreview() {
    Theme {
        ProductListScreenContent(
            state = previewGridState.copy(
                layoutMode = ProductListLayoutMode.COLUMN,
                compactColumnCount = 1,
                nonCompactColumnCount = 2
            ),
            products = previewProducts(items = List(size = 3) { previewEntry(index = it) }),
            searchQuery = null,
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductListLoadingPreview() {
    Theme {
        ProductListScreenContent(
            state = previewGridState.copy(isLoadingMetadata = true),
            products = previewProducts(items = emptyList(), refresh = LoadState.Loading),
            searchQuery = null,
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductListErrorPreview() {
    Theme {
        ProductListScreenContent(
            state = previewGridState,
            products = previewProducts(
                items = emptyList(),
                refresh = LoadState.Error(Throwable("preview"))
            ),
            searchQuery = null,
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductListEmptyPreview() {
    Theme {
        ProductListScreenContent(
            state = previewGridState.copy(resultCount = 0),
            products = previewProducts(items = emptyList()),
            searchQuery = null,
            onEvent = {}
        )
    }
}

// endregion
