package au.com.alfie.ecomm.designsystem.component.topbar.custom

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.test.HOME_TITLE_HEADER
import au.com.alfie.ecomm.core.ui.test.SEARCH_BACK_BUTTON
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.animation.DefaultVisibilityAnimation
import au.com.alfie.ecomm.designsystem.animation.standardAccelerate
import au.com.alfie.ecomm.designsystem.component.divider.DividerType
import au.com.alfie.ecomm.designsystem.component.divider.HorizontalDivider
import au.com.alfie.ecomm.designsystem.component.searchbar.SearchState
import au.com.alfie.ecomm.designsystem.component.searchbar.SearchTextField
import au.com.alfie.ecomm.designsystem.component.searchbar.rememberSearchState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.component.topbar.action.TopBarActions
import au.com.alfie.ecomm.designsystem.component.topbar.component.DefaultNavigationIcon
import au.com.alfie.ecomm.designsystem.component.topbar.custom.LandingHeaderType.Greeting
import au.com.alfie.ecomm.designsystem.component.topbar.custom.LandingHeaderType.Logo
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScope
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScopeInstance
import au.com.alfie.ecomm.designsystem.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScope.LandingHeader(
    type: LandingHeaderType,
    modifier: Modifier = Modifier
) {
    val searchState = state.getSearchState() ?: return

    Column(modifier = modifier.background(topBarColors.containerColor)) {
        LandingHeaderContent(
            type = type,
            isSearchMode = searchState.isSearchOpen
        )
        Search(searchState = searchState)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBarScope.LandingHeaderContent(
    type: LandingHeaderType,
    isSearchMode: Boolean
) {
    TopAppBar(
        title = {
            when (type) {
                is Greeting -> GreetingTopBar(type)
                is Logo -> LogoTopBar(type)
            }
        },
        actions = { TopBarActions(animateVisibility = false) },
        colors = topBarColors,
        modifier = Modifier
            .padding(end = Theme.spacing.spacing12)
            .animateContentSize(standardAccelerate())
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val height = if (isSearchMode) 0 else placeable.height

                layout(
                    width = placeable.width,
                    height = height
                ) {
                    placeable.placeRelative(IntOffset.Zero)
                }
            }
    )
}

@Composable
private fun GreetingTopBar(
    greetingType: Greeting
) {
    Column(modifier = Modifier.testTag(HOME_TITLE_HEADER)) {
        Text(
            text = stringResource(id = R.string.top_bar_greeting, greetingType.userName),
            style = Theme.typography.heading2,
            color = Theme.color.black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        greetingType.subtitle?.let {
            Text(
                text = it,
                style = Theme.typography.small,
                color = Theme.color.primary.mono500
            )
        }
    }
}

@Composable
private fun LogoTopBar(type: Logo) {
    Icon(
        painter = painterResource(id = type.icon),
        contentDescription = type.contentDescription,
        modifier = Modifier
            .height(Theme.iconSize.small)
            .testTag(HOME_TITLE_HEADER)
    )
}

@Composable
private fun TopBarScope.Search(
    searchState: SearchState
) {
    val isSearchMode = searchState.isSearchOpen
    val searchFieldStartPadding by animateDpAsState(
        targetValue = if (isSearchMode) Theme.spacing.spacing0 else Theme.spacing.spacing16,
        animationSpec = standardAccelerate(),
        label = "search field padding"
    )

    Column {
        Row(
            modifier = Modifier
                .run {
                    if (isSearchMode) {
                        this.height(64.dp)
                    } else {
                        this
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            DefaultNavigationIcon(
                modifier = Modifier
                    .size(48.dp)
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
                        end = Theme.spacing.spacing16
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
@Preview
@Composable
private fun LandingHeaderPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Custom(rememberSearchState()) {},
        showNavigationIcon = true
    )

    TopBarScopeInstance(
        state = topBarState,
        topBarColors = TopAppBarDefaults.topAppBarColors()
    ).LandingHeader(
        type = Greeting("User", "Member since: 1838")
    )
}
