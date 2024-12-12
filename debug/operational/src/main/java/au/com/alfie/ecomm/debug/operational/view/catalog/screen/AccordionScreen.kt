package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.component.accordion.Accordion
import au.com.alfie.ecomm.designsystem.component.switch.Switch
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AccordionScreen(
    topBarState: TopBarState
) {
    topBarState.logoTopBar(showNavigationIcon = true)

    var isEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(Theme.spacing.spacing8)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Accordion Small",
            style = Theme.typography.heading3
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Accordion(
            title = "Delivery & Returns",
            isEnabled = isEnabled,
            content = {
                Text(
                    text = "Blandit ut netus consequat ridiculus mi.",
                    style = Theme.typography.small,
                    color = Theme.color.primary.mono700
                )
            }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Accordion(
            title = "Material & Product Care",
            isEnabled = isEnabled,
            content = {
                Text(
                    text = "Blandit ut netus consequat ridiculus mi.",
                    style = Theme.typography.small,
                    color = Theme.color.primary.mono700
                )
            }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing32))
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Accordion Large",
            style = Theme.typography.heading3
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Accordion(
            title = "Delivery & Returns",
            isEnabled = isEnabled,
            content = {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Rhoncus, accumsan, vel interdum diam " +
                        "tortor cursus nam quisque ut. Blandit ut netus consequat ridiculus mi. Lacus a fermentum nec nisl " +
                        "consectetur molestie. Mauris mi cursus quis risus aliquam vivamus blandit. Maecenas dui odio odio aliquet.",
                    style = Theme.typography.small,
                    color = Theme.color.primary.mono700
                )
            },
            isLarge = true
        )
        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Accordion(
            title = "Material & Product Care",
            isEnabled = isEnabled,
            content = {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Rhoncus, accumsan, vel interdum diam " +
                        "tortor cursus nam quisque ut. Blandit ut netus consequat ridiculus mi. Lacus a fermentum nec nisl " +
                        "consectetur molestie. Mauris mi cursus quis risus aliquam vivamus blandit. Maecenas dui odio odio aliquet.",
                    style = Theme.typography.small,
                    color = Theme.color.primary.mono700
                )
            },
            isLarge = true
        )

        Spacer(modifier = Modifier.height(Theme.spacing.spacing12))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = isEnabled,
                onCheckChange = { isEnabled = it }
            )
            Text(
                modifier = Modifier.padding(Theme.spacing.spacing12),
                text = "Enabled",
                style = Theme.typography.paragraph
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AccordionScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Accordion Screen"),
        showNavigationIcon = false
    )
    AccordionScreen(topBarState = topBarState)
}
