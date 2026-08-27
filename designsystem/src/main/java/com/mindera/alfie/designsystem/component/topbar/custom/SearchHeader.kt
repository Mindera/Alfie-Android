package com.mindera.alfie.designsystem.component.topbar.custom

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.ui.test.SEARCH_BACK_BUTTON
import com.mindera.alfie.designsystem.animation.DefaultVisibilityAnimation
import com.mindera.alfie.designsystem.animation.standardAccelerate
import com.mindera.alfie.designsystem.component.divider.DividerType
import com.mindera.alfie.designsystem.component.divider.HorizontalDivider
import com.mindera.alfie.designsystem.component.searchbar.SearchState
import com.mindera.alfie.designsystem.component.searchbar.SearchTextField
import com.mindera.alfie.designsystem.component.searchbar.rememberSearchState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.component.topbar.component.DefaultNavigationIcon
import com.mindera.alfie.designsystem.component.topbar.scope.TopBarScope
import com.mindera.alfie.designsystem.component.topbar.scope.TopBarScopeInstance
import com.mindera.alfie.designsystem.tokens.LocalTheme

/**
 * The search entry point that opens the landing screens — Figma "Search" area (Menu Sheet -
 * Level 1, node `I671:78533;17:14837`): the 40dp search field inset by `screen-size/margin` (16)
 * horizontally and `spacing/spacing-2xs` (4) vertically, giving the 48dp band the design
 * annotation asks for ("the same height as the header that appears on levels 2 or 3").
 *
 * Home and Shop level 1 both render this, so the two entry points cannot drift apart.
 *
 * Tapping the field flips [SearchState.isSearchOpen]; the app shell turns that into the full
 * search overlay, and the band then grows a back affordance and a bottom divider.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScope.SearchHeader(
    searchState: SearchState,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    val isSearchMode = searchState.isSearchOpen
    // The back button takes over the leading inset in search mode, so the field slides flush
    // against it rather than keeping its own screen margin.
    val searchFieldStartPadding by animateDpAsState(
        targetValue = if (isSearchMode) theme.spacing.spacing0 else theme.spacing.spacing16,
        animationSpec = standardAccelerate(),
        label = "search field padding"
    )

    // The band paints its own surface: while the search overlay is open it sits above the scrim,
    // which would otherwise show through (Figma renders the header opaque over the results).
    Column(modifier = modifier.background(topBarColors.containerColor)) {
        Row(
            modifier = if (isSearchMode) {
                Modifier.height(theme.spacing.spacing64)
            } else {
                Modifier.padding(vertical = theme.spacing.spacing4)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            DefaultNavigationIcon(
                modifier = Modifier
                    .size(theme.spacing.spacing48)
                    .testTag(SEARCH_BACK_BUTTON),
                isVisible = isSearchMode,
                enterTransition = fadeIn() + expandIn(standardAccelerate()),
                exitTransition = shrinkOut(standardAccelerate()) + fadeOut(),
                onNavigationClick = {
                    searchState.updateSearchState(false)
                }
            )
            SearchTextField(
                state = searchState,
                isEnabled = isSearchMode,
                modifier = Modifier
                    .padding(
                        start = searchFieldStartPadding,
                        end = theme.spacing.spacing16
                    ),
                onClick = {
                    searchState.updateSearchState(true)
                }
            )
        }
        DefaultVisibilityAnimation(
            isVisible = isSearchMode,
            enterTransition = fadeIn(standardAccelerate()),
            exitTransition = fadeOut(standardAccelerate())
        ) {
            HorizontalDivider(dividerType = DividerType.Solid1Mono100)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun SearchHeaderPreview() {
    val searchState = rememberSearchState()
    val topBarState = TopBarState(
        title = TopBarTitle.Custom(searchState) {},
        showNavigationIcon = false
    )

    TopBarScopeInstance(
        state = topBarState,
        topBarColors = TopAppBarDefaults.topAppBarColors()
    ).SearchHeader(searchState = searchState)
}
