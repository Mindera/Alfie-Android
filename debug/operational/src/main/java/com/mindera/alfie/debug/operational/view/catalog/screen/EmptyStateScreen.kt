package com.mindera.alfie.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.debug.operational.view.catalog.util.HeaderDivider
import com.mindera.alfie.designsystem.component.state.EmptyState
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun EmptyStateScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderDivider(text = "Empty State", modifier = Modifier.fillMaxWidth())
        EmptyState(
            message = "Your bag is empty.",
            icon = AlfieIcons.Bag,
            modifier = Modifier.weight(1f)
        )
        HeaderDivider(text = "Long message", modifier = Modifier.fillMaxWidth())
        EmptyState(
            message = "Nothing saved yet. Tap the heart on a product to keep it here for later.",
            icon = AlfieIcons.Wishlist,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStateScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Empty State"),
        showNavigationIcon = false
    )
    Theme {
        EmptyStateScreen(topBarState = topBarState)
    }
}
