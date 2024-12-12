package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.component.switch.Switch
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun SwitchScreen(
    topBarState: TopBarState
) {
    var isChecked by remember { mutableStateOf(true) }

    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.spacing8)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Switch",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Switch(
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing8),
            checked = isChecked,
            onCheckChange = { isChecked = it }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Switch on disabled",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Switch(
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing8),
            checked = true,
            enabled = false,
            onCheckChange = {}
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Switch off disabled",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Switch(
            modifier = Modifier.padding(horizontal = Theme.spacing.spacing8),
            checked = false,
            enabled = false,
            onCheckChange = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SwitchScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Switch"),
        showNavigationIcon = false
    )
    SwitchScreen(topBarState = topBarState)
}
