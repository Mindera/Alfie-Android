package au.com.alfie.ecomm.designsystem.component.radio

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun LabeledRadioButton(
    isSelected: Boolean,
    label: String,
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    horizontalPadding: Dp = Theme.spacing.spacing16
) {
    val radioColor = if (isEnabled && isSelected) Theme.color.primary.mono050 else Theme.color.primary.mono100
    val borderColor = if (isEnabled) Theme.color.primary.mono900 else Theme.color.primary.mono400

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton,
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
                    shape = Theme.shape.full
                )
                .size(21.dp)
                .background(
                    color = radioColor,
                    shape = Theme.shape.full
                )
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .clip(Theme.shape.full)
                        .background(color = borderColor)
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

@Preview(showBackground = true)
@Composable
private fun LabeledRadioButtonPreview() {
    Theme {
        Column {
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
}
