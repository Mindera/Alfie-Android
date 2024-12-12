package au.com.alfie.ecomm.designsystem.component.chip

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
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme
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
                            painter = painterResource(id = R.drawable.ic_action_close_dark),
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

private fun getTextColor(
    isEnabled: Boolean
): Color {
    return if (isEnabled) {
        Theme.color.primary.mono900
    } else {
        Theme.color.primary.mono300
    }
}

@Composable
private fun chipColors() = FilterChipDefaults.filterChipColors().copy(
    containerColor = Color.Transparent,
    labelColor = Theme.color.primary.mono700,
    disabledContainerColor = Theme.color.primary.mono050,
    disabledLabelColor = Theme.color.primary.mono300,
    selectedContainerColor = Color.Transparent,
    selectedLabelColor = Theme.color.primary.mono900
)

@Composable
private fun chipBorder(isEnabled: Boolean, isSelected: Boolean): BorderStroke {
    val (border, color) = if (isSelected && isEnabled) {
        Pair(2.dp, Theme.color.primary.mono900)
    } else if (isEnabled.not() && isSelected.not()) {
        Pair(0.dp, Theme.color.primary.mono050)
    } else {
        val defaultChipBorder = FilterChipDefaults.filterChipBorder(
            enabled = isEnabled,
            selected = isSelected
        )
        Pair(defaultChipBorder.width, Theme.color.primary.mono900)
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
