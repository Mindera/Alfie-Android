package au.com.alfie.ecomm.debug.operational.view.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import au.com.alfie.ecomm.debug.operational.R
import au.com.alfie.ecomm.designsystem.component.bottombar.BottomBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
internal fun CatalogScreen(
    navigator: DestinationsNavigator,
    topBarState: TopBarState,
    bottomBarState: BottomBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)
    bottomBarState.hideBottomBar()

    LazyColumn {
        item {
            Text(
                modifier = Modifier.padding(
                    start = Theme.spacing.spacing16,
                    top = Theme.spacing.spacing20,
                    bottom = Theme.spacing.spacing8
                ),
                text = stringResource(id = R.string.debug_screen_catalog),
                style = Theme.typography.paragraph,
                color = Theme.color.primary.mono700
            )
        }

        items(CatalogDestination.entries) {
            ListItem(
                headlineContent = {
                    Text(
                        text = it.title,
                        style = Theme.typography.paragraph,
                        color = Theme.color.primary.mono700
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                },
                modifier = Modifier.clickable { navigator.navigate(it.direction) }
            )
            HorizontalDivider()
        }
    }
}
