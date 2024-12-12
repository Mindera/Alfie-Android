package au.com.alfie.ecomm.designsystem.component.topbar.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.component.topbar.action.TopBarActions
import au.com.alfie.ecomm.designsystem.component.topbar.scope.TopBarScope
import au.com.alfie.ecomm.designsystem.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBarScope.BasicTopBar(
    onNavigationClick: ClickEvent,
    modifier: Modifier = Modifier,
    elevation: Dp = Theme.elevation.elevation2,
    content: @Composable () -> Unit
) {
    Surface(shadowElevation = elevation) {
        TopAppBar(
            title = content,
            navigationIcon = {
                NavigationIcon(onNavigationClick = onNavigationClick)
            },
            actions = { TopBarActions() },
            modifier = modifier.padding(end = Theme.spacing.spacing12),
            colors = topBarColors
        )
    }
}
