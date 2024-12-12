package au.com.alfie.ecomm.designsystem.component.topbar.component

import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.test.HOME_TITLE_HEADER
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScope
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScopeInstance
import au.com.alfie.ecomm.designsystem.theme.Theme

private val PADDING_END_DEFAULT = 16.dp
private val PADDING_ICON_EQUIVALENT = Theme.iconSize.large
private val PADDING_START_NO_BACK_ONE_ICON = 24.dp // 8 + 16 (default)
private val PADDING_START_NO_BACK_TWO_ICONS = 56.dp // 32 (icon) + 8 + 16 (default)
private val PADDING_END_NO_BACK_WITH_ICONS = 8.dp // to avoid overlap with actions

@Composable
internal fun TopBarScope.TextTopBar(
    onNavigationClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    val title = state.title as? TopBarTitle.Text ?: TopBarTitle.Text.EMPTY

    if (title.isLeftAligned) {
        LeftAlignedTopBar(
            title = title,
            onNavigationClick = onNavigationClick,
            modifier = modifier
        )
    } else {
        CenterTopBar(
            title = title,
            onNavigationClick = onNavigationClick,
            modifier = modifier
        )
    }
}

@Composable
private fun TopBarScope.CenterTopBar(
    title: TopBarTitle.Text,
    onNavigationClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    val isSearchOpen = state.isSearchOpen()
    // Because we need to use always the TopAppBar instead of use CenterAlignedTopAppBar to the centered text, we need to calculate the paddings depending on all the possible conditions that we have in the topBar
    // Lets hope that the center aligned topbar is removed in some further iteration of the designs and we can remove this block of code
    val modifierTitle = when {
        state.showNavigationIcon.not() && state.actions.isEmpty() -> Modifier
            .fillMaxWidth()
            .padding(end = PADDING_END_DEFAULT)
        state.showNavigationIcon && state.actions.isEmpty() -> Modifier
            .fillMaxWidth()
            .padding(end = PADDING_ICON_EQUIVALENT)
        state.showNavigationIcon.not() && isSearchOpen.not() && state.actions.size == 1 -> Modifier
            .fillMaxWidth()
            .padding(
                start = PADDING_START_NO_BACK_ONE_ICON,
                end = PADDING_END_NO_BACK_WITH_ICONS
            )
        state.showNavigationIcon.not() && isSearchOpen.not() && state.actions.size > 1 -> Modifier
            .fillMaxWidth()
            .padding(
                start = PADDING_START_NO_BACK_TWO_ICONS,
                end = PADDING_END_NO_BACK_WITH_ICONS
            )
        state.showNavigationIcon && isSearchOpen.not() && state.actions.size > 1 -> Modifier
            .fillMaxWidth()
            .padding(start = PADDING_ICON_EQUIVALENT)
        else -> Modifier.fillMaxWidth()
    }

    BasicTopBar(
        onNavigationClick = onNavigationClick,
        modifier = modifier
    ) {
        SearchHandler(
            modifier = modifierTitle,
            contentAlignment = Alignment.Center,
            exitTransition = ExitTransition.None
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(HOME_TITLE_HEADER),
                text = title.title,
                style = Theme.typography.paragraphLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TopBarScope.LeftAlignedTopBar(
    title: TopBarTitle.Text,
    onNavigationClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    BasicTopBar(
        onNavigationClick = onNavigationClick,
        modifier = modifier
    ) {
        SearchHandler(
            modifier = Modifier.fillMaxWidth(),
            exitTransition = ExitTransition.None
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(HOME_TITLE_HEADER),
                text = title.title,
                style = Theme.typography.heading1,
                textAlign = TextAlign.Start
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TextTopBarPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Title"),
        showNavigationIcon = true
    )

    TopBarScopeInstance(
        state = topBarState,
        topBarColors = TopAppBarDefaults.topAppBarColors()
    ).TextTopBar(onNavigationClick = {})
}
