package com.mindera.alfie.debug.operational.view.catalog.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.ui.util.stringResource
import com.mindera.alfie.designsystem.component.divider.DividerType
import com.mindera.alfie.designsystem.component.divider.VerticalDivider
import com.mindera.alfie.designsystem.component.tab.ScrollableTabPager
import com.mindera.alfie.designsystem.component.tab.TabItem
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Destination
@Composable
fun ScrollableTabScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)
    val intrinsicItems = persistentListOf(
        TabItem(StringResource.fromText("Shop All Women's Sale")),
        TabItem(StringResource.fromText("Shop All Men's Sale")),
        TabItem(StringResource.fromText("Shop All Kid's Sale")),
        TabItem(
            label = StringResource.fromText("Top Sale Offers"),
            icon = AlfieIcons.LegacySale
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = Theme.spacing.spacing16)
    ) {
        ScrollableTabSection(
            title = "4 tabs intrinsic width with standard padding",
            items = intrinsicItems,
            isCompact = true
        )
        ScrollableTabSection(
            title = "4 tabs intrinsic width with standard padding and dark indicator",
            items = intrinsicItems,
            isCompact = true,
            isLight = false
        )
        ScrollableTabSection(
            title = "3 tabs intrinsic width with standard padding",
            items = intrinsicItems.subList(fromIndex = 0, toIndex = 3),
            isCompact = true
        )
        ScrollableTabSection(
            title = "3 tabs intrinsic width with expanded padding",
            items = intrinsicItems.subList(fromIndex = 0, toIndex = 3),
            isCompact = false
        )
        ScrollableTabSection(
            title = "2 tabs intrinsic width with standard padding",
            items = intrinsicItems.subList(fromIndex = 0, toIndex = 2),
            isCompact = true
        )
    }
}

@Composable
private fun ScrollableTabSection(
    title: String,
    items: ImmutableList<TabItem>,
    isCompact: Boolean,
    isLight: Boolean = true
) {
    Column {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = title,
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        ScrollableTabPager(
            items = items,
            isCompact = isCompact,
            isLight = isLight
        ) { page ->
            PagerContent(text = stringResource(resource = items[page].label))
        }
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
    }
}

@Composable
private fun PagerContent(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        VerticalDivider(dividerType = DividerType.Solid1Mono200)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(160.dp)
                .background(LocalTheme.current.primitive.colors.neutrals300),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text)
        }
        VerticalDivider(dividerType = DividerType.Solid1Mono200)
    }
}

@Preview(showBackground = true)
@Composable
private fun ScrollableTabScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Tab Screen"),
        showNavigationIcon = false
    )
    ScrollableTabScreen(topBarState = topBarState)
}
