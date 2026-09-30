package com.mindera.alfie.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.debug.operational.view.catalog.util.HeaderDivider
import com.mindera.alfie.designsystem.component.inlinemessage.InlineMessage
import com.mindera.alfie.designsystem.component.topbar.TopBarState
import com.mindera.alfie.designsystem.component.topbar.TopBarTitle
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun InlineMessageScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16),
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = Theme.spacing.spacing24)
    ) {
        HeaderDivider(text = "Inline Message")
        InlineMessage(
            message = "Only 2 items left!",
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
        )
        InlineMessage(
            message = "Sorry, this item is not available anymore. Add it to your wishlist in case it gets back in stock.",
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
        )
        InlineMessage(
            message = "Custom icon",
            icon = AlfieIcons.FastDelivery,
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing16)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InlineMessageScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Inline Message"),
        showNavigationIcon = false
    )
    Theme {
        InlineMessageScreen(topBarState = topBarState)
    }
}
