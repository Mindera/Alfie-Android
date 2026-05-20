package com.mindera.alfie.designsystem.component.topbar.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.component.searchbar.rememberSearchState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.component.topbar.scope.TopBarScope
import com.mindera.alfie.designsystem.component.topbar.scope.TopBarScopeInstance
import com.mindera.alfie.designsystem.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBarScope.SearchTopBar(
    onNavigationClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    Surface(shadowElevation = Theme.elevation.elevation2) {
        TopAppBar(
            title = {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = modifier
                        .padding(end = Theme.spacing.spacing12)
                        .fillMaxWidth()
                ) {
                    ShowSearchTextField()
                }
            },
            navigationIcon = {
                NavigationIcon(
                    onNavigationClick = {
                        if (state.isSearchOpen()) {
                            (state.title as? TopBarTitle.Search)?.searchState?.invertSearchOpenState()
                        } else {
                            onNavigationClick()
                        }
                    }
                )
            },
            colors = topBarColors
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun SearchTopBarPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Search(
            isPullDownToRefresh = true,
            searchState = rememberSearchState(),
            onFocusChange = {},
            onTermChanged = {},
            customOverlay = { _, _ -> }
        ),
        showNavigationIcon = false
    )

    TopBarScopeInstance(
        state = topBarState,
        topBarColors = TopAppBarDefaults.topAppBarColors()
    ).SearchTopBar(onNavigationClick = {})
}
