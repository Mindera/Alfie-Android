package com.mindera.alfie.feature.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.core.ui.test.SEARCH_CLEAR_RECENT_SEARCH
import com.mindera.alfie.core.ui.test.SEARCH_EMPTY_SCREEN
import com.mindera.alfie.core.ui.test.SEARCH_RECENT_SEARCH_ITEM
import com.mindera.alfie.core.ui.test.SEARCH_RECENT_SEARCH_REMOVE_ITEM
import com.mindera.alfie.core.ui.test.SEARCH_RECENT_SEARCH_TITLE
import com.mindera.alfie.designsystem.component.overlay.OverlayLayout
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.feature.search.model.SearchEvent
import com.mindera.alfie.feature.search.model.SearchEvent.OnClearRecentSearches
import com.mindera.alfie.feature.search.model.SearchEvent.OnDeleteRecentSearch
import com.mindera.alfie.feature.search.model.SearchEvent.OnRecentSearchClick
import com.mindera.alfie.feature.uievent.UIEvent
import com.mindera.alfie.feature.uievent.handle
import com.mindera.alfie.feature.uievent.handleUIEvents
import com.mindera.alfie.repository.search.model.RecentSearch
import com.mindera.alfie.designsystem.R as RD

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
                    modifier = Modifier.animateItem()
                )
            }
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
private fun ContentOverlaySearchEmptyPreview() {
    ContentOverlaySearch(
        onSearchEvent = {},
        recentSearches = emptyList()
    )
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
