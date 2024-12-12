package au.com.alfie.ecomm.feature.shop.category

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
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.test.CATEGORY_ITEM
import au.com.alfie.ecomm.core.ui.util.stringResource
import au.com.alfie.ecomm.designsystem.component.divider.DividerType
import au.com.alfie.ecomm.designsystem.component.divider.HorizontalDivider
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEntryUI
import au.com.alfie.ecomm.feature.shop.ui.EntryHeadlineContent
import kotlinx.collections.immutable.ImmutableList
import au.com.alfie.ecomm.designsystem.R as RD

internal fun LazyListScope.categoryItems(
    entries: ImmutableList<CategoryEntryUI>,
    isPlaceholder: Boolean,
    onEntryClick: ClickEventOneArg<CategoryEntryUI>
) {
    itemsIndexed(
        items = entries,
        key = { index, _ -> index }
    ) { _, entry ->
        Box(
            modifier = Modifier
                .clickable { onEntryClick(entry) }
                .testTag(CATEGORY_ITEM)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Theme.spacing.spacing16)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    EntryHeadlineContent(
                        text = stringResource(resource = entry.title),
                        isLoading = isPlaceholder
                    )
                }
                Icon(
                    painter = painterResource(id = RD.drawable.ic_action_chevron_right),
                    contentDescription = null,
                    modifier = Modifier.size(Theme.iconSize.small)
                )
            }
        }
        HorizontalDivider(
            dividerType = DividerType.Solid1Mono100,
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
        )
    }
}
