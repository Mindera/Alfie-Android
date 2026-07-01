package com.mindera.alfie.designsystem.component.chip

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.designsystem.R
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import androidx.compose.material3.FilterChip as MaterialChip

private const val MAXIMUM_COUNTER_VALUE = 99
private val CHIP_HEIGHT_REGULAR = 32.dp
private val CHIP_HEIGHT_LARGE = 44.dp

@Composable
fun Chip(
    label: String,
    isSelected: Boolean,
    onClickEvent: ClickEvent,
    chipType: ChipType = ChipType.REGULAR,
    isEnabled: Boolean = true,
    isDismissible: Boolean = false,
    onDismiss: ClickEvent = {},
    counter: Int? = null
) {
    val counterText = counter?.let {
        if (counter > MAXIMUM_COUNTER_VALUE) stringResource(id = R.string.chip_count_limit) else "$counter"
    }.orEmpty()
    val chipLabel = "$label $counterText".trim()

    val modifier = if (chipType == ChipType.REGULAR) {
        Modifier.height(CHIP_HEIGHT_REGULAR)
    } else {
        Modifier.height(CHIP_HEIGHT_LARGE)
    }

    MaterialChip(
        modifier = modifier,
        selected = isSelected,
        onClick = onClickEvent,
        label = {
            Text(
                text = chipLabel,
                color = getTextColor(isEnabled = isEnabled)
            )
        },
        trailingIcon = {
            AnimatedVisibility(visible = isSelected && isDismissible) {
                IconButton(
                    modifier = Modifier.size(Theme.iconSize.small),
                    enabled = isEnabled,
                    onClick = onDismiss,
                    content = {
                        Icon(
                            painter = painterResource(id = AlfieIcons.Close),
                            contentDescription = null
                        )
                    }
                )
            }
        },
        enabled = isEnabled,
        colors = chipColors(),
        border = chipBorder(
            isEnabled = isEnabled,
            isSelected = isSelected
        ),
        shape = Theme.shape.full
    )
}

@Composable
private fun getTextColor(
    isEnabled: Boolean
): Color {
    val c = LocalTheme.current.primitive.colors
    return if (isEnabled) {
        c.neutrals800
    } else {
        c.neutrals300
    }
}

@Composable
private fun chipColors(): androidx.compose.material3.SelectableChipColors {
    val c = LocalTheme.current.primitive.colors
    return FilterChipDefaults.filterChipColors().copy(
        containerColor = Color.Transparent,
        labelColor = c.neutrals600,
        disabledContainerColor = c.neutrals100,
        disabledLabelColor = c.neutrals300,
        selectedContainerColor = Color.Transparent,
        selectedLabelColor = c.neutrals800
    )
}

@Composable
private fun chipBorder(isEnabled: Boolean, isSelected: Boolean): BorderStroke {
    val c = LocalTheme.current.primitive.colors
    val (border, color) = if (isSelected && isEnabled) {
        Pair(2.dp, c.neutrals800)
    } else if (isEnabled.not() && isSelected.not()) {
        Pair(0.dp, c.neutrals100)
    } else {
        val defaultChipBorder = FilterChipDefaults.filterChipBorder(
            enabled = isEnabled,
            selected = isSelected
        )
        Pair(defaultChipBorder.width, c.neutrals800)
    }

    val animatedBorder by animateDpAsState(
        targetValue = border,
        label = "animatedBorder"
    )
    val animatedColor by animateColorAsState(
        targetValue = color,
        label = "animatedColor"
    )

    return BorderStroke(
        width = animatedBorder,
        color = animatedColor
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ChipScreenPreview() {
    Column(modifier = Modifier.padding(Theme.spacing.spacing16)) {
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
    }
}
