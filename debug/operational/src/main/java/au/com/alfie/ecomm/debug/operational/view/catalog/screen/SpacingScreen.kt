package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

private var spacings = listOf(
    Theme.spacing.spacing0 to "spacing0",
    Theme.spacing.spacing2 to "spacing2",
    Theme.spacing.spacing4 to "spacing4",
    Theme.spacing.spacing6 to "spacing6",
    Theme.spacing.spacing8 to "spacing8",
    Theme.spacing.spacing12 to "spacing12",
    Theme.spacing.spacing16 to "spacing16",
    Theme.spacing.spacing20 to "spacing20",
    Theme.spacing.spacing24 to "spacing24",
    Theme.spacing.spacing32 to "spacing32",
    Theme.spacing.spacing40 to "spacing40",
    Theme.spacing.spacing48 to "spacing48",
    Theme.spacing.spacing56 to "spacing56",
    Theme.spacing.spacing64 to "spacing64",
    Theme.spacing.spacing72 to "spacing72",
    Theme.spacing.spacing80 to "spacing80"
)

private const val COLUMN_COUNT = 4

@Destination
@Composable
fun SpacingScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.spacing12),
        columns = GridCells.Fixed(COLUMN_COUNT)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                modifier = Modifier.padding(Theme.spacing.spacing12),
                text = "Spacing",
                style = Theme.typography.heading3
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            HorizontalDivider()
            Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        }
        items(spacings) { spacing ->
            SpacingItem(
                spacing = spacing.first,
                name = spacing.second
            )
        }
    }
}

@Composable
private fun SpacingItem(spacing: Dp, name: String) {
    Column(
        modifier = Modifier.padding(vertical = Theme.spacing.spacing12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = Theme.spacing.spacing2)
                .fillMaxWidth()
                .height(spacing)
                .background(Theme.color.secondary.green300)
        )
        Text(
            text = name,
            style = Theme.typography.small
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SpacingScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Spacing Screen"),
        showNavigationIcon = false
    )
    SpacingScreen(topBarState = topBarState)
}
