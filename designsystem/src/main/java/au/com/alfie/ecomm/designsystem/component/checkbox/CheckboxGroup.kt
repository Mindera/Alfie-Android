package au.com.alfie.ecomm.designsystem.component.checkbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun CheckboxGroup(
    options: List<CheckboxProperties>,
    onSelectionChange: (index: Int, properties: CheckboxProperties, isChecked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkboxSpacing: Dp = 0.dp,
    horizontalPadding: Dp = Theme.spacing.spacing16
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(checkboxSpacing),
        modifier = modifier
    ) {
        options.forEachIndexed { index, properties ->
            LabeledCheckbox(
                properties = properties,
                onCheckedChange = { isChecked -> onSelectionChange(index, properties, isChecked) },
                horizontalPadding = horizontalPadding
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckboxGroupPreview() {
    Theme {
        CheckboxGroup(
            options = listOf(
                CheckboxProperties(
                    isChecked = false,
                    label = "Unselected"
                ),
                CheckboxProperties(
                    isChecked = true,
                    label = "Selected"
                ),
                CheckboxProperties(
                    isChecked = false,
                    label = "Unselected + Disabled",
                    isEnabled = false
                ),
                CheckboxProperties(
                    isChecked = true,
                    label = "Selected + Disabled",
                    isEnabled = false
                )
            ),
            onSelectionChange = { _, _, _ -> }
        )
    }
}
