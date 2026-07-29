package com.mindera.alfie.feature.shop.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.core.ui.test.CATEGORY_ITEM
import com.mindera.alfie.core.ui.util.stringResource
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI
import com.mindera.alfie.feature.shop.ui.EntryHeadlineContent
import kotlinx.collections.immutable.ImmutableList

internal fun LazyListScope.categoryItems(
    entries: ImmutableList<CategoryEntryUI>,
    isPlaceholder: Boolean,
    onEntryClick: ClickEventOneArg<CategoryEntryUI>
) {
    // Keyed by the Room row id rather than position, so reordering or inserting entries cannot
    // cause the wrong row to be reused. Placeholder entries carry distinct ids (0 until
    // PLACEHOLDER_COUNT) and are never mixed with real ones.
    itemsIndexed(
        items = entries,
        key = { _, entry -> entry.id }
    ) { _, entry ->
        val theme = LocalTheme.current
        Box(
            modifier = Modifier
                .clickable { onEntryClick(entry) }
                .testTag(CATEGORY_ITEM)
        ) {
            // 12 dp vertical padding over a 24 dp line box gives the 48 dp row pitch in the design,
            // which holds whether or not the chevron is present.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = theme.spacing.spacing16,
                        vertical = theme.spacing.spacing12
                    )
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    EntryHeadlineContent(
                        text = stringResource(resource = entry.title),
                        isLoading = isPlaceholder
                    )
                }
                // Only rows that drill into sub-categories carry a chevron; leaves open the listing
                // directly. Shares CategoryEntryUI.hasChildren with the tap branch in
                // NavigateToEntryDelegate so the affordance and the destination cannot disagree.
                if (entry.hasChildren) {
                    Icon(
                        painter = painterResource(id = AlfieIcons.ChevronRight),
                        contentDescription = null,
                        tint = theme.color.content.contentPrimary,
                        modifier = Modifier.size(theme.sizing.icon.medium)
                    )
                }
            }
        }
    }
}
