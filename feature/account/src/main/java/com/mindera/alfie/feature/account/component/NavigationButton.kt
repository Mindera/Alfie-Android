package com.mindera.alfie.feature.account.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.test.ACCOUNT_ACTION_ICON
import com.mindera.alfie.core.ui.test.ACCOUNT_SECTION_ICON
import com.mindera.alfie.designsystem.component.divider.DividerType
import com.mindera.alfie.designsystem.component.divider.HorizontalDivider
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.feature.account.model.NavigationButtonUI

@Composable
internal fun NavigationButton(
    item: NavigationButtonUI,
    onClickEvent: ClickEvent,
    horizontalPadding: Dp = Theme.spacing.spacing16
) {
    Box(
        modifier = Modifier.clickable { onClickEvent() }
    ) {
        Column {
            ListItem(
                modifier = Modifier.testTag(item.testTag),
                leadingContent = {
                    Icon(
                        modifier = Modifier
                            .size(Theme.iconSize.small)
                            .testTag(ACCOUNT_SECTION_ICON),
                        painter = painterResource(id = item.icon),
                        contentDescription = null
                    )
                },
                headlineContent = {
                    Text(
                        text = stringResource(id = item.title),
                        style = Theme.typography.paragraph
                    )
                },
                trailingContent = {
                    Icon(
                        modifier = Modifier
                            .padding(end = horizontalPadding)
                            .size(Theme.iconSize.small)
                            .testTag(ACCOUNT_ACTION_ICON),
                        painter = painterResource(id = AlfieIcons.ChevronRight),
                        contentDescription = null
                    )
                }
            )
            HorizontalDivider(dividerType = DividerType.Solid1Mono300)
        }
    }
}
