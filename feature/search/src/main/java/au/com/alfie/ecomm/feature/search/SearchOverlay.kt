package au.com.alfie.ecomm.feature.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.extension.highlightTerm
import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.core.ui.test.SEARCH_CLEAR_RECENT_SEARCH
import au.com.alfie.ecomm.core.ui.test.SEARCH_EMPTY_SCREEN
import au.com.alfie.ecomm.core.ui.test.SEARCH_NO_RESULTS_SCREEN
import au.com.alfie.ecomm.core.ui.test.SEARCH_RECENT_SEARCH_ITEM
import au.com.alfie.ecomm.core.ui.test.SEARCH_RECENT_SEARCH_REMOVE_ITEM
import au.com.alfie.ecomm.core.ui.test.SEARCH_RECENT_SEARCH_TITLE
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonSize
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.component.loading.Loading
import au.com.alfie.ecomm.designsystem.component.loading.LoadingType
import au.com.alfie.ecomm.designsystem.component.overlay.OverlayLayout
import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCard
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.search.model.BrandSuggestionUI
import au.com.alfie.ecomm.feature.search.model.KeywordSuggestionUI
import au.com.alfie.ecomm.feature.search.model.ProductSuggestionUI
import au.com.alfie.ecomm.feature.search.model.SearchEvent
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnClearRecentSearches
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnDeleteRecentSearch
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnOpenSearchScreen
import au.com.alfie.ecomm.feature.search.model.SearchEvent.OnRecentSearchClick
import au.com.alfie.ecomm.feature.search.model.SearchUI
import au.com.alfie.ecomm.feature.search.model.SearchUIState
import au.com.alfie.ecomm.feature.uievent.UIEvent
import au.com.alfie.ecomm.feature.uievent.handle
import au.com.alfie.ecomm.feature.uievent.handleUIEvents
import au.com.alfie.ecomm.repository.search.model.RecentSearch
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import au.com.alfie.ecomm.designsystem.R as RD

private const val PRODUCTS_COLUMN_COUNT = 2

@Composable
fun SearchOverlay(
    isOpen: Boolean,
    onSearchAction: ((String) -> Unit) -> Unit,
    onUpdateSearchTerm: ((String) -> Unit) -> Unit,
    navController: NavController,
    directionProvider: DirectionProvider,
    modifier: Modifier = Modifier,
    onDismiss: ClickEvent = {},
    content: @Composable () -> Unit
) {
    val viewModel = viewModel<SearchViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()

    onUpdateSearchTerm { term ->
        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(term))
    }
    onSearchAction { term ->
        viewModel.handleEvent(SearchEvent.OnSearchAction(term))
    }

    LaunchedEffect(isOpen) {
        if (isOpen) {
            viewModel.handleEvent(OnOpenSearchScreen)
        }
    }

    viewModel.handleUIEvents { uiEvent ->
        when (uiEvent) {
            is UIEvent.Base.NavigateToScreen -> uiEvent.handle(
                navController = navController,
                directionProvider = directionProvider
            )
            is UIEvent.Base.NavigateToScreenClearingStack -> uiEvent.handle(
                navController = navController,
                directionProvider = directionProvider
            )
            else -> { /* No action */ }
        }
        onDismiss()
    }

    OverlayLayout(
        isOpen = isOpen,
        onDismiss = onDismiss,
        overlayContent = {
            ContentOverlaySearch(
                state = state,
                onSearchEvent = viewModel::handleEvent,
                recentSearches = recentSearches
            )
        },
        modifier = modifier,
        content = content
    )
}

@Composable
private fun ContentOverlaySearch(
    state: SearchUIState,
    onSearchEvent: ClickEventOneArg<SearchEvent>,
    recentSearches: List<RecentSearch>
) {
    when (state) {
        is SearchUIState.Empty -> SearchNoContent(
            onSearchEvent = onSearchEvent,
            recentSearches = recentSearches
        )

        is SearchUIState.Error -> SearchError(
            searchTerm = state.searchTerm,
            onSearchEvent = onSearchEvent
        )
        is SearchUIState.Loaded -> SearchContent(
            searchUI = state.searchUI,
            onSearchEvent = onSearchEvent
        )
        is SearchUIState.Loading -> Loading(
            type = LoadingType.Dark,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun SearchContent(
    searchUI: SearchUI,
    onSearchEvent: ClickEventOneArg<SearchEvent>
) {
    val hasKeywords = searchUI.keywords.isNotEmpty()
    val hasBrands = searchUI.brands.isNotEmpty()
    val hasProducts = searchUI.products.isNotEmpty()

    LazyVerticalGrid(
        columns = GridCells.Fixed(PRODUCTS_COLUMN_COUNT),
        contentPadding = PaddingValues(
            horizontal = Theme.spacing.spacing16,
            vertical = Theme.spacing.spacing8
        ),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16)
    ) {
        if (hasKeywords) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SuggestionsSectionTitle(title = stringResource(id = R.string.search_suggestions_keyword_title))
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                SuggestionsSectionList(
                    suggestions = searchUI.keywords.map { it.value }.toImmutableList(),
                    searchTerm = searchUI.searchTerm,
                    onSuggestionClick = { onSearchEvent(SearchEvent.OnKeywordSuggestionClick(searchUI.keywords[it])) }
                )
            }
            if (hasBrands || hasProducts) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
                }
            }
        }
        if (hasBrands) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SuggestionsSectionTitle(title = stringResource(id = R.string.search_suggestions_brand_title))
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                SuggestionsSectionList(
                    suggestions = searchUI.brands.map { it.name }.toImmutableList(),
                    searchTerm = searchUI.searchTerm,
                    onSuggestionClick = { onSearchEvent(SearchEvent.OnBrandSuggestionClick(searchUI.brands[it])) }
                )
            }
            if (hasProducts) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
                }
            }
        }
        if (hasProducts) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SuggestionsSectionTitle(title = stringResource(id = R.string.search_suggestions_product_title))
            }
            items(searchUI.products) { suggestion ->
                ProductCard(
                    productCardType = suggestion.productCardData,
                    onClick = { onSearchEvent(SearchEvent.OnProductSuggestionClick(suggestion)) },
                    modifier = Modifier.padding(vertical = Theme.spacing.spacing6)
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Button(
                        type = ButtonType.Secondary,
                        buttonSize = ButtonSize.Medium,
                        text = stringResource(id = R.string.search_suggestions_more_products_cta),
                        onClick = { onSearchEvent(SearchEvent.OnMoreProductsClick) },
                        modifier = Modifier.padding(top = Theme.spacing.spacing12)
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionsSectionTitle(title: String) {
    Text(
        text = title,
        style = Theme.typography.paragraphLarge,
        modifier = Modifier.padding(
            top = Theme.spacing.spacing8,
            bottom = Theme.spacing.spacing12
        )
    )
}

@Composable
private fun SuggestionsSectionList(
    suggestions: ImmutableList<String>,
    searchTerm: String,
    onSuggestionClick: ClickEventOneArg<Int>
) {
    Column(
        modifier = Modifier.padding(horizontal = Theme.spacing.spacing8),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing8)
    ) {
        suggestions.forEachIndexed { index, suggestion ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSuggestionClick(index) }
            ) {
                Text(
                    text = suggestion.highlightTerm(searchTerm),
                    style = Theme.typography.paragraph,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Theme.spacing.spacing4)
                )
            }
        }
    }
}

@Composable
private fun SearchNoContent(
    onSearchEvent: ClickEventOneArg<SearchEvent>,
    recentSearches: List<RecentSearch>
) {
    if (recentSearches.isNotEmpty()) {
        RecentSearchesPanel(
            onSearchEvent = onSearchEvent,
            recentSearches = recentSearches
        )
    } else {
        SearchEmpty()
    }
}

@Composable
private fun SearchEmpty() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(294.dp)
                .testTag(SEARCH_EMPTY_SCREEN)
        ) {
            Icon(
                painter = painterResource(id = RD.drawable.ic_action_search_dark),
                contentDescription = null,
                tint = Theme.color.black,
                modifier = Modifier.size(Theme.iconSize.large)
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            Text(
                text = stringResource(R.string.search_empty_title),
                style = Theme.typography.paragraphBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
            Text(
                text = stringResource(R.string.search_empty_description),
                style = Theme.typography.small,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecentSearchesPanel(
    onSearchEvent: ClickEventOneArg<SearchEvent>,
    recentSearches: List<RecentSearch>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = Theme.spacing.spacing8),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(state = rememberLazyListState()) {
            item { RecentSearchesTitle(onSearchEvent = onSearchEvent) }
            items(
                items = recentSearches,
                key = { it.searchTerm }
            ) {
                RecentSearchItem(
                    recentSearch = it,
                    onSearchEvent = onSearchEvent,
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    }
}

@Composable
private fun SearchError(
    searchTerm: String,
    onSearchEvent: ClickEventOneArg<SearchEvent>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(SEARCH_NO_RESULTS_SCREEN)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = Theme.spacing.spacing16,
                vertical = Theme.spacing.spacing12
            )
        ) {
            Text(
                text = stringResource(id = R.string.no_results_text1, searchTerm),
                style = Theme.typography.paragraph
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing32))
            Text(
                text = stringResource(id = R.string.no_results_text2),
                style = Theme.typography.paragraph
            )
            Spacer(modifier = Modifier.height(Theme.spacing.spacing32))
            Text(
                text = stringResource(id = R.string.no_results_cta),
                style = Theme.typography.paragraphUnderlined,
                modifier = Modifier.clickable { onSearchEvent(SearchEvent.OnViewAllBrandsClick) }
            )
        }
    }
}

@Composable
private fun RecentSearchesTitle(onSearchEvent: ClickEventOneArg<SearchEvent>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Theme.spacing.spacing16,
                end = Theme.spacing.spacing4
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.testTag(SEARCH_RECENT_SEARCH_TITLE),
            text = stringResource(R.string.your_recent_searches),
            style = Theme.typography.heading3
        )
        TextButton(
            modifier = Modifier.testTag(SEARCH_CLEAR_RECENT_SEARCH),
            onClick = { onSearchEvent(OnClearRecentSearches) }
        ) {
            Text(
                text = stringResource(R.string.clear),
                style = Theme.typography.paragraphBoldUnderline,
                color = Theme.color.primary.mono900
            )
        }
    }
}

@Composable
private fun RecentSearchItem(
    recentSearch: RecentSearch,
    onSearchEvent: ClickEventOneArg<SearchEvent>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSearchEvent(OnRecentSearchClick(recentSearch)) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(SEARCH_RECENT_SEARCH_ITEM),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = Theme.spacing.spacing16,
                        horizontal = Theme.spacing.spacing24
                    )
                    .weight(1f),
                text = recentSearch.searchTerm,
                style = Theme.typography.paragraph,
                color = Theme.color.primary.mono900,
                maxLines = 1,
                overflow = Ellipsis
            )
            IconButton(
                modifier = Modifier
                    .padding(end = Theme.spacing.spacing16)
                    .size(Theme.iconSize.large)
                    .testTag(SEARCH_RECENT_SEARCH_REMOVE_ITEM),
                onClick = { onSearchEvent(OnDeleteRecentSearch(recentSearch)) }
            ) {
                Icon(
                    painter = painterResource(id = RD.drawable.ic_action_close_dark),
                    modifier = Modifier.size(Theme.iconSize.small),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun ContentOverlaySearchNoContentPreview() {
    val state = SearchUIState.Empty
    ContentOverlaySearch(
        state = state,
        onSearchEvent = {},
        recentSearches = emptyList()
    )
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun ContentOverlaySearchNoResultsPreview() {
    val state = SearchUIState.Error("polo")
    ContentOverlaySearch(
        state = state,
        onSearchEvent = {},
        recentSearches = emptyList()
    )
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun ContentOverlayRecentSearchesPreview() {
    val state = SearchUIState.Empty
    ContentOverlaySearch(
        state = state,
        onSearchEvent = {},
        recentSearches = listOf(
            RecentSearch.Query(searchTerm = "Recent #1"),
            RecentSearch.Query(searchTerm = "Recent #2"),
            RecentSearch.Query(searchTerm = "Recent #3"),
            RecentSearch.Query(searchTerm = "Recent #4"),
            RecentSearch.Query(searchTerm = "A very long search string that will probably overflow")
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun ContentOverlaySearchSuggestionsPreview() {
    val searchUI = SearchUI(
        searchTerm = "Polo",
        keywords = listOf(
            KeywordSuggestionUI("Polo Ralph Lauren Men"),
            KeywordSuggestionUI("Polo Ralph Lauren Women"),
            KeywordSuggestionUI("Polo Ralph Lauren"),
            KeywordSuggestionUI("Bear Polo")
        ),
        brands = listOf(
            BrandSuggestionUI(
                name = "Polo Ralph Lauren",
                slug = "polo-ralph-lauren"
            )
        ),
        products = List(size = 8) {
            ProductSuggestionUI(
                id = "$it",
                slug = "polo",
                productCardData = ProductCardType.Small(
                    image = ImageUI(
                        images = persistentListOf(ImageSizeUI.Large("https://images.pexels.com/photos/2297720/pexels-photo-2297720.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")),
                        alt = null
                    ),
                    brand = "POLO RALPH LAUREN",
                    name = "MENS CUSTOM FIT BUTTON DOWN OXFORD SHIRT",
                    price = PriceType.Default("$100")
                )
            )
        }
    )
    val state = SearchUIState.Loaded(searchUI)
    Theme {
        ContentOverlaySearch(
            state = state,
            onSearchEvent = { },
            recentSearches = emptyList()
        )
    }
}
