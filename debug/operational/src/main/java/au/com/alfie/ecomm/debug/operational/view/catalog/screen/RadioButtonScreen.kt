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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.component.radio.LabeledRadioButton
import au.com.alfie.ecomm.designsystem.component.radio.RadioButtonGroup
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarState
import au.com.alfie.ecomm.designsystem.component.topbar.TopBarTitle
import au.com.alfie.ecomm.designsystem.theme.Theme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun RadioButtonScreen(topBarState: TopBarState) {
    topBarState.logoTopBar(showNavigationIcon = true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        LabeledRadioButtons()
        Spacer(modifier = Modifier.height(12.dp))
        EnabledRadioButtonGroup()
        Spacer(modifier = Modifier.height(12.dp))
        DisabledRadioButtonGroup()
    }
}

@Composable
fun LabeledRadioButtons() {
    Column(modifier = Modifier.padding(Theme.spacing.spacing12)) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Radio Button",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        LabeledRadioButton(
            isSelected = false,
            label = "Unselected",
            onClick = { }
        )
        LabeledRadioButton(
            isSelected = true,
            label = "Selected",
            onClick = { }
        )
        LabeledRadioButton(
            isSelected = false,
            label = "Unselected + Disabled",
            isEnabled = false,
            onClick = { }
        )
        LabeledRadioButton(
            isSelected = true,
            label = "Selected + Disabled",
            isEnabled = false,
            onClick = { }
        )
    }
}

@Composable
fun EnabledRadioButtonGroup() {
    var optionSelected by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.padding(Theme.spacing.spacing12)) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Radio Button Group (enabled)",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        RadioButtonGroup(
            options = listOf("Option 1", "Option 2", "Option 3"),
            optionSelected = optionSelected,
            onSelectionChange = { optionSelected = it }
        )
    }
}

@Composable
fun DisabledRadioButtonGroup() {
    Column(modifier = Modifier.padding(Theme.spacing.spacing12)) {
        Text(
            modifier = Modifier.padding(Theme.spacing.spacing12),
            text = "Radio Button Group (disabled)",
            style = Theme.typography.heading3
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(Theme.spacing.spacing16))
        RadioButtonGroup(
            options = listOf("Option 1", "Option 2", "Option 3"),
            optionSelected = 0,
            onSelectionChange = { },
            isEnabled = { false }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioButtonScreenPreview() {
    val topBarState = TopBarState(
        title = TopBarTitle.Text("Radio Button Screen"),
        showNavigationIcon = false
    )
    Theme {
        RadioButtonScreen(topBarState = topBarState)
    }
}
