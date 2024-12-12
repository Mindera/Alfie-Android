package au.com.alfie.ecomm.designsystem.component.topbar.action

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.test.HOME_ACCOUNT_BUTTON
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
internal fun AccountTopBarAction(
    onClick: ClickEvent
) {
    IconButton(
        modifier = Modifier
            .size(Theme.iconSize.large)
            .testTag(HOME_ACCOUNT_BUTTON),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_action_user),
            contentDescription = null,
            modifier = Modifier.size(Theme.iconSize.medium)
        )
    }
}
