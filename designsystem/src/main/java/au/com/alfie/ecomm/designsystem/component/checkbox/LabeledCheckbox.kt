package au.com.alfie.ecomm.designsystem.component.checkbox

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun LabeledCheckbox(
    isChecked: Boolean,
    label: String,
    onCheckedChange: ClickEventOneArg<Boolean>,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    horizontalPadding: Dp = Theme.spacing.spacing16
) {
    val checkboxColor by animateColorAsState(
        targetValue = when {
            isEnabled && isChecked.not() -> Theme.color.primary.mono100
            isEnabled && isChecked -> Theme.color.primary.mono900
            isEnabled.not() && isChecked -> Theme.color.primary.mono400
            else -> Theme.color.primary.mono100
        },
        label = "CheckboxColor"
    )
    val borderColor = if (isEnabled) Theme.color.primary.mono900 else Theme.color.primary.mono400

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .toggleable(
                value = isChecked,
                onValueChange = onCheckedChange,
                role = Role.Checkbox,
                enabled = isEnabled
            )
            .padding(horizontal = horizontalPadding)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = Theme.shape.tiny
                )
                .size(24.dp)
                .background(
                    color = checkboxColor,
                    shape = Theme.shape.tiny
                )
        ) {
            if (isChecked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_informational_checkmark),
                    contentDescription = null,
                    tint = Theme.color.primary.mono050,
                    modifier = Modifier.size(Theme.iconSize.small)
                )
            }
        }
        Spacer(modifier = Modifier.width(Theme.spacing.spacing12))
        Text(
            text = label,
            style = Theme.typography.paragraph
        )
    }
}

@Composable
fun LabeledCheckbox(
    properties: CheckboxProperties,
    onCheckedChange: ClickEventOneArg<Boolean>,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = Theme.spacing.spacing16
) {
    LabeledCheckbox(
        isChecked = properties.isChecked,
        label = properties.label,
        onCheckedChange = onCheckedChange,
        isEnabled = properties.isEnabled,
        modifier = modifier,
        horizontalPadding = horizontalPadding
    )
}

@Preview(showBackground = true)
@Composable
private fun LabeledCheckboxPreview() {
    Theme {
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
}
