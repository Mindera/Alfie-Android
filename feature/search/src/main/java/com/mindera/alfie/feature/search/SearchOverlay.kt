package com.mindera.alfie.feature.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.core.ui.extension.highlightTerm
import com.mindera.alfie.core.ui.media.image.ImageSizeUI
import com.mindera.alfie.core.ui.media.image.ImageUI
import com.mindera.alfie.core.ui.test.SEARCH_CLEAR_RECENT_SEARCH
import com.mindera.alfie.core.ui.test.SEARCH_NO_RESULTS_SCREEN
import com.mindera.alfie.core.ui.test.SEARCH_RECENT_SEARCH_ITEM
import com.mindera.alfie.core.ui.test.SEARCH_RECENT_SEARCH_REMOVE_ITEM
import com.mindera.alfie.core.ui.test.SEARCH_RECENT_SEARCH_TITLE
import com.mindera.alfie.core.ui.test.SEARCH_SUGGESTIONS_BRAND_ITEM
import com.mindera.alfie.core.ui.test.SEARCH_SUGGESTIONS_ERROR
import com.mindera.alfie.core.ui.test.SEARCH_SUGGESTIONS_KEYWORD_ITEM
import com.mindera.alfie.core.ui.test.SEARCH_SUGGESTIONS_LOADING
import com.mindera.alfie.core.ui.test.SEARCH_SUGGESTIONS_MORE_PRODUCTS
import com.mindera.alfie.core.ui.test.SEARCH_SUGGESTIONS_PRODUCT_ITEM
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.component.loading.LoadingSpinner
import com.mindera.alfie.designsystem.component.loading.LoadingSpinnerSize
import com.mindera.alfie.designsystem.component.overlay.OverlayLayout
import com.mindera.alfie.designsystem.component.price.PriceType
import com.mindera.alfie.designsystem.component.productcard.ProductCard
import com.mindera.alfie.designsystem.component.productcard.ProductCardType
import com.mindera.alfie.designsystem.component.state.StateMessage
import com.mindera.alfie.designsystem.component.state.StateMessageAction
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.search.model.BrandSuggestionUI
import com.mindera.alfie.feature.search.model.KeywordSuggestionUI
import com.mindera.alfie.feature.search.model.ProductSuggestionUI
import com.mindera.alfie.feature.search.model.SearchEvent
import com.mindera.alfie.feature.search.model.SearchEvent.OnBrandSuggestionClick
import com.mindera.alfie.feature.search.model.SearchEvent.OnClearRecentSearches
import com.mindera.alfie.feature.search.model.SearchEvent.OnDeleteRecentSearch
import com.mindera.alfie.feature.search.model.SearchEvent.OnKeywordSuggestionClick
import com.mindera.alfie.feature.search.model.SearchEvent.OnMoreProductsClick
import com.mindera.alfie.feature.search.model.SearchEvent.OnRecentSearchClick
import com.mindera.alfie.feature.search.model.SearchEvent.OnRetrySuggestions
import com.mindera.alfie.feature.search.model.SearchUI
import com.mindera.alfie.feature.search.model.SearchUIState
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.handle
import com.mindera.alfie.feature.uievent.handleUIEvents
import com.mindera.alfie.repository.search.model.RecentSearch
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

private const val PRODUCTS_COLUMN_COUNT = 2

@Composable
fun SearchOverlay(
    isOpen: Boolean,
    onSearchAction: ((String) -> Unit) -> Unit,
    onSearchTermChange: ((String) -> Unit) -> Unit,
    navController: NavController,
    directionProvider: DirectionProvider,
    modifier: Modifier = Modifier,
    onDismiss: ClickEvent = {},
    content: @Composable () -> Unit
) {
    val viewModel = viewModel<SearchViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()

    onSearchAction { term ->
        viewModel.handleEvent(SearchEvent.OnSearchAction(term))
    }
    onSearchTermChange { term ->
        viewModel.handleEvent(SearchEvent.OnUpdateSearchTerm(term))
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
            SearchOverlayContent(
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
private fun SearchOverlayContent(
    state: SearchUIState,
    onSearchEvent: ClickEventOneArg<SearchEvent>,
    recentSearches: List<RecentSearch>
) {
    when (state) {
        is SearchUIState.Empty -> RecentSearchesPanel(
            onSearchEvent = onSearchEvent,
            recentSearches = recentSearches
        )
        is SearchUIState.Loading -> Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag(SEARCH_SUGGESTIONS_LOADING),
            contentAlignment = Alignment.Center
        ) {
            LoadingSpinner(size = LoadingSpinnerSize.Medium)
        }
        is SearchUIState.NoResults -> StateMessage(
            modifier = Modifier.testTag(SEARCH_NO_RESULTS_SCREEN),
            title = stringResource(R.string.search_suggestions_no_results_title, state.searchTerm),
            subtitle = stringResource(R.string.search_suggestions_no_results_subtitle)
        )
        is SearchUIState.Error -> StateMessage(
            modifier = Modifier.testTag(SEARCH_SUGGESTIONS_ERROR),
            title = stringResource(R.string.search_suggestions_error_title),
            subtitle = stringResource(R.string.search_suggestions_error_subtitle, state.searchTerm),
            action = StateMessageAction(
                label = stringResource(R.string.search_suggestions_error_cta),
                onClick = { onSearchEvent(OnRetrySuggestions) }
            )
        )
        is SearchUIState.Loaded -> SuggestionsPanel(
            searchUI = state.searchUI,
            onSearchEvent = onSearchEvent
        )
    }
}

@Composable
private fun SuggestionsPanel(
    searchUI: SearchUI,
    onSearchEvent: ClickEventOneArg<SearchEvent>
) {
    val spacing = LocalTheme.current.spacing
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(spacing.spacing16),
        verticalArrangement = Arrangement.spacedBy(spacing.spacing8)
    ) {
        // Platforms fill sections unevenly (BigCommerce suggests products only), so each section
        // renders only when it has entries.
        if (searchUI.keywords.isNotEmpty()) {
            item { SuggestionsSectionTitle(title = stringResource(R.string.search_suggestions_keyword_title)) }
            items(items = searchUI.keywords, key = { "keyword-${it.value}" }) { keyword ->
                SuggestionRow(
                    text = keyword.value,
                    searchTerm = searchUI.searchTerm,
                    testTag = SEARCH_SUGGESTIONS_KEYWORD_ITEM,
                    onClick = { onSearchEvent(OnKeywordSuggestionClick(keyword)) }
                )
            }
        }
        if (searchUI.brands.isNotEmpty()) {
            item { SuggestionsSectionTitle(title = stringResource(R.string.search_suggestions_brand_title)) }
            items(items = searchUI.brands, key = { "brand-${it.key}" }) { brand ->
                SuggestionRow(
                    text = brand.name,
                    searchTerm = searchUI.searchTerm,
                    testTag = SEARCH_SUGGESTIONS_BRAND_ITEM,
                    onClick = { onSearchEvent(OnBrandSuggestionClick(brand)) }
                )
            }
        }
        if (searchUI.products.isNotEmpty()) {
            productSuggestions(
                products = searchUI.products,
                onSearchEvent = onSearchEvent
            )
        }
    }
}

private fun LazyListScope.productSuggestions(
    products: List<ProductSuggestionUI>,
    onSearchEvent: ClickEventOneArg<SearchEvent>
) {
    item { SuggestionsSectionTitle(title = stringResource(R.string.search_suggestions_product_title)) }
    items(
        items = products.chunked(PRODUCTS_COLUMN_COUNT),
        key = { row -> "products-${row.first().id}" }
    ) { row ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(LocalTheme.current.spacing.spacing8)
        ) {
            row.forEach { product ->
                ProductCard(
                    modifier = Modifier
                        .weight(1f)
                        .testTag(SEARCH_SUGGESTIONS_PRODUCT_ITEM),
                    productCardType = product.productCardData
                )
            }
            // Keeps a lone trailing card at column width instead of letting it stretch.
            repeat(PRODUCTS_COLUMN_COUNT - row.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = LocalTheme.current.spacing.spacing8),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier.testTag(SEARCH_SUGGESTIONS_MORE_PRODUCTS),
                type = ButtonType.Secondary,
                buttonSize = ButtonSize.Medium,
                text = stringResource(R.string.search_suggestions_more_products_cta),
                onClick = { onSearchEvent(OnMoreProductsClick) }
            )
        }
    }
}

@Composable
private fun SuggestionsSectionTitle(title: String) {
    Text(
        modifier = Modifier.padding(top = LocalTheme.current.spacing.spacing8),
        text = title,
        style = LocalTheme.current.typography.heading.xSmall,
        color = LocalTheme.current.color.content.contentPrimary
    )
}

@Composable
private fun SuggestionRow(
    text: String,
    searchTerm: String,
    testTag: String,
    onClick: ClickEvent
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = LocalTheme.current.spacing.spacing8),
        text = text.highlightTerm(searchTerm),
        style = LocalTheme.current.typography.body.medium,
        color = LocalTheme.current.color.content.contentPrimary,
        maxLines = 1,
        overflow = Ellipsis
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecentSearchesPanel(
    onSearchEvent: ClickEventOneArg<SearchEvent>,
    recentSearches: List<RecentSearch>
) {
    // The panel always occupies the overlay so the Surface behind it stays full-bleed —
    // an empty list renders as blank space rather than letting the scrim show through.
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(
            top = LocalTheme.current.spacing.spacing16,
            start = LocalTheme.current.spacing.spacing16,
            end = LocalTheme.current.spacing.spacing16,
            bottom = LocalTheme.current.spacing.spacing16
        )
    ) {
        if (recentSearches.isNotEmpty()) {
            item { RecentSearchesTitle(onSearchEvent = onSearchEvent) }
        }
        items(
            items = recentSearches,
            key = { it.searchTerm }
        ) {
            RecentSearchItem(
                recentSearch = it,
                onSearchEvent = onSearchEvent,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun RecentSearchesTitle(onSearchEvent: ClickEventOneArg<SearchEvent>) {
    val c = LocalTheme.current.color
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = LocalTheme.current.spacing.spacing8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.testTag(SEARCH_RECENT_SEARCH_TITLE),
            text = stringResource(R.string.your_recent_searches),
            style = LocalTheme.current.typography.heading.xSmall,
            color = c.content.contentPrimary
        )
        // Figma "Link": link/medium (Medium 510) underlined in link-primary, flush at the
        // content inset. Figma sizes the link as bare text; Role.Button keeps TalkBack honest.
        Text(
            modifier = Modifier
                .testTag(SEARCH_CLEAR_RECENT_SEARCH)
                .clickable(role = Role.Button) { onSearchEvent(OnClearRecentSearches) },
            text = stringResource(R.string.clear),
            style = LocalTheme.current.typography.link.medium.copy(textDecoration = TextDecoration.Underline),
            color = c.link.linkPrimaryDefault
        )
    }
}

@Composable
private fun RecentSearchItem(
    recentSearch: RecentSearch,
    onSearchEvent: ClickEventOneArg<SearchEvent>,
    modifier: Modifier = Modifier
) {
    val c = LocalTheme.current.color.content
    val sizing = LocalTheme.current.sizing.icon
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button) { onSearchEvent(OnRecentSearchClick(recentSearch)) }
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
                    .padding(vertical = LocalTheme.current.spacing.spacing8)
                    .weight(1f),
                text = recentSearch.searchTerm,
                style = LocalTheme.current.typography.body.medium,
                color = c.contentPrimary,
                maxLines = 1,
                overflow = Ellipsis
            )
            IconButton(
                modifier = Modifier
                    .size(sizing.large)
                    .testTag(SEARCH_RECENT_SEARCH_REMOVE_ITEM),
                onClick = { onSearchEvent(OnDeleteRecentSearch(recentSearch)) }
            ) {
                Icon(
                    painter = painterResource(id = AlfieIcons.Close),
                    modifier = Modifier.size(sizing.medium),
                    contentDescription = stringResource(
                        R.string.remove_recent_search,
                        recentSearch.searchTerm
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun RecentSearchesPanelEmptyPreview() {
    RecentSearchesPanel(
        onSearchEvent = {},
        recentSearches = emptyList()
    )
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun RecentSearchesPanelPreview() {
    RecentSearchesPanel(
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
private fun SuggestionsPanelPreview() {
    Theme {
        SearchOverlayContent(
            state = SearchUIState.Loaded(
                SearchUI(
                    searchTerm = "sh",
                    keywords = persistentListOf(
                        KeywordSuggestionUI(value = "shirt"),
                        KeywordSuggestionUI(value = "theo shirt dress")
                    ),
                    brands = persistentListOf(
                        BrandSuggestionUI(key = "Alfie Basics", name = "Alfie Basics"),
                        BrandSuggestionUI(key = "Kashmere", name = "Kashmere")
                    ),
                    products = List(size = 3) {
                        ProductSuggestionUI(
                            id = "$it",
                            slug = "t-shirt",
                            productCardData = ProductCardType.Vertical(
                                image = ImageUI(
                                    images = persistentListOf(ImageSizeUI.Custom(url = "")),
                                    alt = null
                                ),
                                brand = "Alfie Basics",
                                name = "T-Shirt",
                                price = PriceType.Default(price = "£8.00")
                            )
                        )
                    }.toImmutableList()
                )
            ),
            onSearchEvent = {},
            recentSearches = emptyList()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun SuggestionsNoResultsPreview() {
    Theme {
        SearchOverlayContent(
            state = SearchUIState.NoResults(searchTerm = "zzz"),
            onSearchEvent = {},
            recentSearches = emptyList()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun SuggestionsErrorPreview() {
    Theme {
        SearchOverlayContent(
            state = SearchUIState.Error(searchTerm = "shirt"),
            onSearchEvent = {},
            recentSearches = emptyList()
        )
    }
}
