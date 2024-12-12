package au.com.alfie.ecomm.feature.account.component

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
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.test.ACCOUNT_ACTION_ICON
import au.com.alfie.ecomm.core.ui.test.ACCOUNT_SECTION_ICON
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.component.divider.DividerType
import au.com.alfie.ecomm.designsystem.component.divider.HorizontalDivider
import au.com.alfie.ecomm.designsystem.theme.Theme
import au.com.alfie.ecomm.feature.account.model.NavigationButtonUI

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
                        painter = painterResource(id = R.drawable.ic_action_chevron_right),
                        contentDescription = null
                    )
                }
            )
            HorizontalDivider(dividerType = DividerType.Solid1Mono300)
        }
    }
}
