package com.mindera.alfie.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mindera.alfie.debug.operational.view.catalog.util.HeaderDivider
import com.mindera.alfie.debug.operational.view.catalog.util.SwitchItem
import com.mindera.alfie.designsystem.component.searchbar.rememberSearchState
import com.mindera.alfie.designsystem.component.topbar.TopBar
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.action.TopBarAction
import com.mindera.alfie.designsystem.component.topbar.custom.LandingHeader
import com.mindera.alfie.designsystem.component.topbar.custom.LandingHeaderType
import com.mindera.alfie.designsystem.component.topbar.rememberTopBarState
import com.mindera.alfie.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf

@Destination
@Composable
internal fun LandingHeaderScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    var isLogged by remember { mutableStateOf(true) }
    val landingPageTopBarState = rememberTopBarState()
    val actions = persistentListOf(
        TopBarAction.Account {}
    )

    val type = if (isLogged) {
        LandingHeaderType.Greeting(
            userName = "Alfie",
            subtitle = "Member since: 1838"
        )
    } else {
        LandingHeaderType.Logo()
    }

    landingPageTopBarState.customTopBar(
        searchState = rememberSearchState(),
        actions = actions
    ) {
        LandingHeader(type = type)
    }

    Column {
        Spacer(modifier = Modifier.size(Theme.spacing.spacing24))
        HeaderDivider(
            text = "Landing Page Header",
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
        )
        SwitchItem(
            text = "Logged User",
            isChecked = isLogged,
            onCheckChange = { isLogged = it }
        )
        Spacer(modifier = Modifier.size(Theme.spacing.spacing24))
        TopBar(
            state = landingPageTopBarState,
            onNavigationClick = {}
        )
    }
}
