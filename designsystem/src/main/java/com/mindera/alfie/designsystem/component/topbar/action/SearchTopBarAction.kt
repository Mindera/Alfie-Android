package com.mindera.alfie.designsystem.component.topbar.action

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.test.HOME_SEARCH_BUTTON
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.icons.AlfieIcons

@Composable
internal fun SearchTopBarAction(
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(Theme.iconSize.large)
            .testTag(HOME_SEARCH_BUTTON)
    ) {
        Icon(
            painter = painterResource(id = AlfieIcons.Search),
            contentDescription = contentDescription,
            modifier = Modifier.size(Theme.iconSize.medium)
        )
    }
}

@Preview
@Composable
private fun SearchTopBarActionPreview() {
    Theme {
        SearchTopBarAction(onClick = { })
    }
}
