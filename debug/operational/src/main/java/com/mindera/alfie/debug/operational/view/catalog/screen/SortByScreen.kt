package com.mindera.alfie.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.component.sortby.SortByItem
import com.mindera.alfie.designsystem.component.sortby.SortBySelector
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import com.mindera.alfie.designsystem.icons.AlfieIcons

@Destination
@Composable
fun SortByScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(Theme.spacing.spacing6))
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing16),
            text = "Sort By Component",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing40))

        val items = persistentListOf(
            SortByItem(
                text = "Most Popular",
                icon = AlfieIcons.Wishlist
            ),
            SortByItem(
                text = "Price - High to Low",
                icon = AlfieIcons.LegacySale
            ),
            SortByItem(
                text = "Price - Low to High",
                icon = AlfieIcons.LegacySale
            ),
            SortByItem(
                text = "New-in",
                icon = AlfieIcons.LegacyStore
            ),
            SortByItem(text = "A-Z"),
            SortByItem(text = "Z-A")
        )
        var selectedItem by remember { mutableIntStateOf(0) }

        Text(
            text = "Sort By",
            style = Theme.typography.paragraphLarge,
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        SortBySelector(
            selectedIndex = selectedItem,
            items = items,
            onItemSelection = { selectedItem = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SortByScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("SortBy Component Screen"),
        showNavigationIcon = false
    )
    Theme {
        SortByScreen(topBarState = topBarState)
    }
}
