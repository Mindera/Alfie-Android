package au.com.alfie.ecomm.designsystem.component.topbar.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.test.HOME_TITLE_HEADER
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScope
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScopeInstance
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
internal fun TopBarScope.IconTopBar(
    onNavigationClick: ClickEvent,
    modifier: Modifier = Modifier
) {
    val title = state.title as? TopBarTitle.Icon ?: TopBarTitle.Icon.EMPTY

    BasicTopBar(
        onNavigationClick = onNavigationClick,
        modifier = modifier
    ) {
        SearchHandler(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                painter = painterResource(id = title.icon),
                contentDescription = title.contentDescription,
                modifier = Modifier
                    .height(Theme.iconSize.small)
                    .testTag(HOME_TITLE_HEADER)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun IconTopBarPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Icon(icon = R.drawable.ic_alfie_logo_dark),
        showNavigationIcon = true
    )

    TopBarScopeInstance(
        state = topBarState,
        topBarColors = TopAppBarDefaults.topAppBarColors()
    ).IconTopBar(onNavigationClick = {})
}
