package au.com.alfie.ecomm.designsystem.component.topbar.action

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.test.HOME_SEARCH_BUTTON
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme

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
            painter = painterResource(id = R.drawable.ic_action_search_dark),
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
