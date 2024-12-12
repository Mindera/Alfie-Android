package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.component.tag.Tag
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination
import au.com.alfie.ecomm.designsystem.R as RD

@Destination
@Composable
fun TagScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Theme.spacing.spacing16)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Tag",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        Tag(text = "Text Tag")
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Tag(
            text = "Text Tag with Icon",
            icon = RD.drawable.ic_action_star
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Tag(
            text = "Dismissible Text Tag",
            isDismissible = true
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Tag(
            text = "Dismissible Text Tag With Icon",
            icon = RD.drawable.ic_action_star,
            isDismissible = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TagScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Tag Screen"),
        showNavigationIcon = false
    )
    TagScreen(topBarState = topBarState)
}
