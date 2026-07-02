package com.mindera.alfie.designsystem.component.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.component.topbar.component.IconTopBar
import com.mindera.alfie.designsystem.component.topbar.component.SearchTopBar
import com.mindera.alfie.designsystem.component.topbar.component.TextTopBar
import com.mindera.alfie.designsystem.component.topbar.scope.TopBarScopeInstance
import com.mindera.alfie.designsystem.tokens.LocalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    state: TopBarState,
    onNavigationClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    if (state.isVisible.not()) return

    val title = state.title
    val topBarColors = if (state.isDarkTheme) {
        darkThemeAppBarColors()
    } else {
        lightThemeAppBarColors()
    }
    val topBarScope = TopBarScopeInstance(
        state = state,
        topBarColors = topBarColors
    )

    when (title) {
        is TopBarTitle.Text -> topBarScope.TextTopBar(
            onNavigationClick = onNavigationClick,
            modifier = modifier
        )
        is TopBarTitle.Icon -> topBarScope.IconTopBar(
            onNavigationClick = onNavigationClick,
            modifier = modifier
        )
        is TopBarTitle.Search -> topBarScope.SearchTopBar(
            onNavigationClick = onNavigationClick,
            modifier = modifier
        )
        is TopBarTitle.Custom -> title.custom(topBarScope)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun darkThemeAppBarColors(): TopAppBarColors {
    val c = LocalTheme.current.primitive.colors
    return TopAppBarDefaults.topAppBarColors().copy(
        containerColor = c.neutrals600,
        scrolledContainerColor = c.neutrals600,
        navigationIconContentColor = c.neutrals100,
        titleContentColor = c.neutrals100,
        actionIconContentColor = c.neutrals100
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun lightThemeAppBarColors(): TopAppBarColors {
    val c = LocalTheme.current.primitive.colors
    return TopAppBarDefaults.topAppBarColors()
        .copy(actionIconContentColor = c.neutrals800)
}
