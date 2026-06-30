package com.mindera.alfie.designsystem.component.topbar.action

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.test.HOME_ACCOUNT_BUTTON
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.icons.AlfieIcons

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
            painter = painterResource(id = AlfieIcons.Account),
            contentDescription = null,
            modifier = Modifier.size(Theme.iconSize.medium)
        )
    }
}
