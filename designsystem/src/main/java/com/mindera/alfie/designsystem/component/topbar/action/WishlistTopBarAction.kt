package com.mindera.alfie.designsystem.component.topbar.action

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme

@Composable
fun WishlistTopBarAction(onClick: ClickEvent) {
    IconButton(
        modifier = Modifier.size(Theme.iconSize.large),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = AlfieIcons.Wishlist),
            contentDescription = null,
            modifier = Modifier.size(Theme.iconSize.medium)
        )
    }
}
