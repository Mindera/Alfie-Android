package com.mindera.alfie.designsystem.component.chip

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme

private const val MAXIMUM_COUNTER_VALUE = 99

@Composable
fun Chip(
    label: String,
    isSelected: Boolean,
    onClickEvent: ClickEvent,
    isEnabled: Boolean = true,
    isDismissible: Boolean = false,
    onDismiss: ClickEvent = {},
    counter: Int? = null
) {
    val theme = LocalTheme.current
    val color = theme.color

    val counterText = counter?.let {
        if (counter > MAXIMUM_COUNTER_VALUE) stringResource(id = R.string.chip_count_limit) else "$counter"
    }.orEmpty()
    val chipLabel = "$label $counterText".trim()

    val shape = theme.sizing.radius.rounded
    val weightDefault = theme.primitive.border.weightDefault

    val targetBackground = if (isSelected) color.surface.backgroundPrimaryActive else Color.Transparent
    val (targetBorderWidth, targetBorderColor) = when {
        isSelected -> 0.dp to Color.Transparent
        !isEnabled -> weightDefault to color.content.contentPrimaryDisabled
        else -> weightDefault to color.border.medium
    }
    val textColor = when {
        !isEnabled -> color.content.contentPrimaryDisabled
        isSelected -> color.content.contentPrimaryActive
        else -> color.content.contentPrimary
    }

    val background by animateColorAsState(targetValue = targetBackground, label = "chipBackground")
    val borderWidth by animateDpAsState(targetValue = targetBorderWidth, label = "chipBorderWidth")
    val borderColor by animateColorAsState(targetValue = targetBorderColor, label = "chipBorderColor")

    Row(
        modifier = Modifier
            .clip(shape)
            .background(color = background, shape = shape)
            .border(width = borderWidth, color = borderColor, shape = shape)
            .selectable(
                selected = isSelected,
                enabled = isEnabled,
                role = Role.Checkbox,
                onClick = onClickEvent
            )
            .padding(
                horizontal = theme.spacing.spacing16,
                vertical = theme.spacing.spacing4
            ),
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.spacing4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = chipLabel,
            style = theme.typography.body.medium,
            color = textColor
        )
        AnimatedVisibility(visible = isSelected && isDismissible) {
            Icon(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .clickable(
                        enabled = isEnabled,
                        role = Role.Button,
                        onClick = onDismiss
                    )
                    .size(theme.sizing.icon.small),
                painter = painterResource(id = AlfieIcons.Close),
                contentDescription = stringResource(id = R.string.chip_dismiss),
                tint = if (isEnabled) color.content.contentPrimaryActive else color.content.contentPrimaryDisabled
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ChipScreenPreview() {
    Theme {
        Column(modifier = Modifier.padding(LocalTheme.current.spacing.spacing16)) {
            Chip(
                label = "Default",
                counter = 12,
                onClickEvent = {},
                isSelected = false
            )
            Chip(
                label = "Selected",
                counter = 1234,
                onClickEvent = {},
                isSelected = true
            )
            Chip(
                label = "Disabled",
                counter = 0,
                onClickEvent = {},
                isSelected = false,
                isEnabled = false
            )
            Chip(
                label = "Disabled Selected",
                onClickEvent = {},
                isSelected = true,
                isEnabled = false
            )
            Chip(
                label = "Dismissible",
                onClickEvent = {},
                isSelected = true,
                isDismissible = true
            )
        }
    }
}
