package au.com.alfie.ecomm.designsystem.component.radio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun RadioButtonGroup(
    options: List<String>,
    optionSelected: Int,
    onSelectionChange: ClickEventOneArg<Int>,
    modifier: Modifier = Modifier,
    isEnabled: (Int) -> Boolean = { true },
    checkboxSpacing: Dp = 0.dp,
    horizontalPadding: Dp = Theme.spacing.spacing16
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(checkboxSpacing),
        modifier = modifier.selectableGroup()
    ) {
        options.forEachIndexed { index, option ->
            LabeledRadioButton(
                isSelected = index == optionSelected,
                label = option,
                isEnabled = isEnabled(index),
                onClick = { onSelectionChange(index) },
                horizontalPadding = horizontalPadding
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioButtonGroupPreview() {
    Theme {
        RadioButtonGroup(
            options = listOf("Option 1", "Option 2", "Option 3"),
            optionSelected = 0,
            onSelectionChange = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioButtonGroupDisabledPreview() {
    Theme {
        RadioButtonGroup(
            options = listOf("Option 1", "Option 2", "Option 3"),
            optionSelected = 0,
            onSelectionChange = { },
            isEnabled = { false }
        )
    }
}
