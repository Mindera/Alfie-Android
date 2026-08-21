package com.mindera.alfie.feature.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.core.ui.test.SEARCH_CLEAR_RECENT_SEARCH
import com.mindera.alfie.core.ui.test.SEARCH_RECENT_SEARCH_ITEM
import com.mindera.alfie.core.ui.test.SEARCH_RECENT_SEARCH_REMOVE_ITEM
import com.mindera.alfie.core.ui.test.SEARCH_RECENT_SEARCH_TITLE
import com.mindera.alfie.designsystem.component.overlay.OverlayLayout
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.search.model.SearchEvent
import com.mindera.alfie.feature.search.model.SearchEvent.OnClearRecentSearches
import com.mindera.alfie.feature.search.model.SearchEvent.OnDeleteRecentSearch
import com.mindera.alfie.feature.search.model.SearchEvent.OnRecentSearchClick
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.handle
import com.mindera.alfie.feature.uievent.handleUIEvents
import com.mindera.alfie.repository.search.model.RecentSearch

@Composable
fun SearchOverlay(
    isOpen: Boolean,
    onSearchAction: ((String) -> Unit) -> Unit,
    navController: NavController,
    directionProvider: DirectionProvider,
    modifier: Modifier = Modifier,
    onDismiss: ClickEvent = {},
    content: @Composable () -> Unit
) {
    val viewModel = viewModel<SearchViewModel>()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()

    onSearchAction { term ->
        viewModel.handleEvent(SearchEvent.OnSearchAction(term))
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
    onSearchEvent: ClickEventOneArg<SearchEvent>,
    recentSearches: List<RecentSearch>
) {
    if (recentSearches.isNotEmpty()) {
        RecentSearchesPanel(
            onSearchEvent = onSearchEvent,
            recentSearches = recentSearches
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecentSearchesPanel(
    onSearchEvent: ClickEventOneArg<SearchEvent>,
    recentSearches: List<RecentSearch>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(
            top = Theme.spacing.spacing16,
            start = Theme.spacing.spacing16,
            end = Theme.spacing.spacing16
        )
    ) {
        item { RecentSearchesTitle(onSearchEvent = onSearchEvent) }
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
    val c = LocalTheme.current.color.content
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Theme.spacing.spacing8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.testTag(SEARCH_RECENT_SEARCH_TITLE),
            text = stringResource(R.string.your_recent_searches),
            style = LocalTheme.current.typography.heading.xSmall,
            color = c.contentPrimary
        )
        Box(
            modifier = Modifier
                .testTag(SEARCH_CLEAR_RECENT_SEARCH)
                .clickable { onSearchEvent(OnClearRecentSearches) }
        ) {
            Text(
                text = stringResource(R.string.clear),
                style = LocalTheme.current.typography.body.mediumBold.copy(textDecoration = TextDecoration.Underline),
                color = c.contentPrimary
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
    val c = LocalTheme.current.color.content
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
                    .padding(vertical = Theme.spacing.spacing8)
                    .weight(1f),
                text = recentSearch.searchTerm,
                style = LocalTheme.current.typography.body.medium,
                color = c.contentPrimary,
                maxLines = 1,
                overflow = Ellipsis
            )
            IconButton(
                modifier = Modifier
                    .size(Theme.iconSize.large)
                    .testTag(SEARCH_RECENT_SEARCH_REMOVE_ITEM),
                onClick = { onSearchEvent(OnDeleteRecentSearch(recentSearch)) }
            ) {
                Icon(
                    painter = painterResource(id = AlfieIcons.Close),
                    modifier = Modifier.size(Theme.iconSize.medium),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun ContentOverlayRecentSearchesPreview() {
    ContentOverlaySearch(
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
