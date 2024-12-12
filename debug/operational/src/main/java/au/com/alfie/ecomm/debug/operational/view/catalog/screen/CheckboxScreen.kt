package au.com.alfie.ecomm.debug.operational.view.catalog.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import au.com.alfie.ecomm.designsystem.component.checkbox.CheckboxGroup
import au.com.alfie.ecomm.designsystem.component.checkbox.CheckboxProperties
import au.com.alfie.ecomm.designsystem.component.checkbox.LabeledCheckbox
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun CheckboxScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Theme.spacing.spacing8)
    ) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Checkbox",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        LabeledCheckboxes()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))

        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Checkbox Group",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        CheckboxGroupExample()
    }
}

@Composable
private fun LabeledCheckboxes() {
    Column {
        LabeledCheckbox(
            isChecked = false,
            label = "Unselected",
            onCheckedChange = { }
        )
        LabeledCheckbox(
            isChecked = true,
            label = "Selected",
            onCheckedChange = { }
        )
        LabeledCheckbox(
            isChecked = false,
            label = "Unselected + Disabled",
            isEnabled = false,
            onCheckedChange = { }
        )
        LabeledCheckbox(
            isChecked = true,
            label = "Selected + Disabled",
            isEnabled = false,
            onCheckedChange = { }
        )
    }
}

@Composable
private fun CheckboxGroupExample() {
    val options = remember {
        mutableStateListOf(
            CheckboxProperties(
                isChecked = false,
                label = "Option 1"
            ),
            CheckboxProperties(
                isChecked = true,
                label = "Option 2"
            ),
            CheckboxProperties(
                isChecked = false,
                label = "Option 3 (Disabled)",
                isEnabled = false
            ),
            CheckboxProperties(
                isChecked = false,
                label = "Option 4"
            )
        )
    }

    CheckboxGroup(
        options = options.toList(),
        onSelectionChange = { index, properties, isChecked ->
            options[index] = properties.copy(isChecked = isChecked)
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CheckboxScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Checkbox Screen"),
        showNavigationIcon = false
    )
    Theme {
        CheckboxScreen(topBarState = topBarState)
    }
}
