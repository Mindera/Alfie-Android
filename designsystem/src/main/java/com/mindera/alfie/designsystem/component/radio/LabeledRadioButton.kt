package com.mindera.alfie.designsystem.component.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

/** DS control geometry: a 24 dp box, a 20 dp ring, and a 10 dp inner dot when selected. */
private val RADIO_ROW_MIN_HEIGHT = 40.dp
private val RADIO_SELECTED_DOT_SIZE = 10.dp

@Composable
fun LabeledRadioButton(
    isSelected: Boolean,
    label: String,
    onClick: ClickEvent,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    horizontalPadding: Dp = Theme.spacing.spacing16
) {
    val theme = LocalTheme.current
    // DS: unselected is a white fill with a #111111 ring; selected drops the fill so only the ring
    // and the inner dot show. These were the wrong way round — unselected rendered foregroundPrimary
    // (#F7F7F7) and selected rendered white — which predates this branch.
    val radioColor = when {
        // The DS defines no disabled state; this keeps the pre-existing muted fill.
        !isEnabled -> theme.color.surface.foregroundPrimary
        isSelected -> theme.primitive.colors.transparent
        else -> theme.color.surface.backgroundPrimary
    }
    val borderColor = if (isEnabled) {
        theme.color.content.contentPrimary
    } else {
        theme.color.content.contentPrimaryDisabled
    }

    // DS row `3022:3850`: label leads, the 24 dp control is trailing, and the selected label switches
    // from body/medium to body/medium-bold — only the weight changes, the colour stays
    // content/content-primary.
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing16),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(RADIO_ROW_MIN_HEIGHT)
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton,
                enabled = isEnabled
            )
            .padding(horizontal = horizontalPadding)
    ) {
        Text(
            text = label,
            style = if (isSelected) theme.typography.body.mediumBold else theme.typography.body.medium,
            color = if (isEnabled) {
                theme.color.content.contentPrimary
            } else {
                theme.color.content.contentPrimaryDisabled
            },
            modifier = Modifier.weight(1f)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(theme.sizing.icon.medium)
                .border(
                    width = theme.primitive.border.weightDefault,
                    color = borderColor,
                    shape = Theme.shape.full
                )
                .background(
                    color = radioColor,
                    shape = Theme.shape.full
                )
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(RADIO_SELECTED_DOT_SIZE)
                        .clip(Theme.shape.full)
                        .background(color = borderColor)
                )
            }
        }
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
