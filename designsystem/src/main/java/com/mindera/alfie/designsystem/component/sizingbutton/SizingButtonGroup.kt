package com.mindera.alfie.designsystem.component.sizingbutton

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.event.ClickEvent
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.button.Button
import com.mindera.alfie.designsystem.component.button.ButtonSize
import com.mindera.alfie.designsystem.component.button.ButtonType
import com.mindera.alfie.designsystem.icons.AlfieIcons
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val MAX_ITEMS_PER_LINE = 3

// `.size-selector-buttons` (PDP size selector, Design System): square 40dp chips on a
// 1px border/soft stroke; selection is a heavier border, never a fill; out-of-stock chips
// dim the label to content/terciary and cross it with a border/soft diagonal falling
// left-to-right, the notification bell inset 3px from the top-trailing corner.
private val CHIP_BORDER = 1.dp
private val CHIP_BORDER_SELECTED = 2.dp
private val BELL_INSET = 3.dp

const val INVALID_INDEX = -1

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SizingButtonGroup(
    options: ImmutableList<SizingButtonProperties>,
    selectedIndex: Int,
    onSelectedOption: ClickEventOneArg<Int>,
    modifier: Modifier = Modifier
) {
    var itemWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                val sumSpacings = with(density) {
                    Theme.spacing.spacing8.toPx() * (MAX_ITEMS_PER_LINE - 1)
                }

                val itemsFullWidth = size.width - sumSpacings
                itemWidth = with(density) {
                    (itemsFullWidth / MAX_ITEMS_PER_LINE).toDp()
                }
            },
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing8),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing8),
        maxItemsInEachRow = MAX_ITEMS_PER_LINE
    ) {
        options.forEachIndexed { index, option ->
            SizingButton(
                buttonText = option.text,
                buttonWidth = itemWidth,
                state = option.state,
                isSelected = selectedIndex == index,
                onClick = { onSelectedOption(index) }
            )
        }
    }
}

@Composable
private fun SizingButton(
    buttonText: String,
    buttonWidth: Dp,
    state: SizingButtonState,
    isSelected: Boolean,
    onClick: ClickEvent
) {
    val c = LocalTheme.current.primitive.colors
    val modifier = Modifier.width(width = buttonWidth)
    when (state) {
        SizingButtonState.Selectable -> {
            if (isSelected) {
                Button(
                    modifier = modifier,
                    type = ButtonType.Secondary,
                    buttonSize = ButtonSize.Medium,
                    text = buttonText,
                    overrideBorderThickness = CHIP_BORDER_SELECTED,
                    overrideTextStyle = LocalTheme.current.typography.body.medium,
                    overrideColors = ButtonColors(
                        containerColor = c.neutrals0,
                        contentColor = c.neutrals200,
                        disabledContainerColor = c.neutrals0,
                        disabledContentColor = c.neutrals200
                    ),
                    overrideTextColor = c.neutrals800,
                    onClick = { Unit }
                )
            } else {
                Button(
                    modifier = modifier,
                    type = ButtonType.Secondary,
                    buttonSize = ButtonSize.Medium,
                    text = buttonText,
                    overrideTextStyle = LocalTheme.current.typography.body.medium,
                    overrideColors = ButtonColors(
                        containerColor = c.neutrals0,
                        contentColor = c.neutrals200,
                        disabledContainerColor = c.neutrals0,
                        disabledContentColor = c.neutrals200
                    ),
                    overrideTextColor = c.neutrals800,
                    onClick = { onClick() }
                )
            }
        }

        SizingButtonState.OutOfStock -> {
            OutOfStockChip(
                text = buttonText,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun OutOfStockChip(
    text: String,
    modifier: Modifier = Modifier
) {
    val c = LocalTheme.current.primitive.colors

    Box(
        modifier = modifier
            .heightIn(min = Theme.spacing.spacing40)
            .border(
                width = CHIP_BORDER,
                color = c.neutrals200,
                shape = Theme.shape.none
            )
            .drawBehind {
                drawLine(
                    color = c.neutrals200,
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = size.width, y = size.height),
                    strokeWidth = CHIP_BORDER.toPx()
                )
            }
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                // The chip is drawn rather than a disabled Button, so it must announce its
                // unavailability itself; the bell stays invisible to accessibility by design.
                .semantics { disabled() },
            text = text,
            style = LocalTheme.current.typography.body.medium,
            color = c.neutrals500
        )
        Icon(
            painter = painterResource(id = AlfieIcons.Notification),
            contentDescription = null, // decorative per design — notify-me has no service yet
            tint = c.neutrals500,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = BELL_INSET, end = BELL_INSET)
                .size(Theme.iconSize.medium)
        )
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun SizingButtonsPreview() {
    Theme {
        SizingButtonGroup(
            selectedIndex = 1,
            options = persistentListOf(
                SizingButtonProperties(
                    text = "Unselected",
                    state = SizingButtonState.Selectable
                ),
                SizingButtonProperties(
                    text = "Selected",
                    state = SizingButtonState.Selectable
                ),
                SizingButtonProperties(
                    text = "Out of Stock",
                    state = SizingButtonState.OutOfStock
                )
            ),
            onSelectedOption = { }
        )
    }
}
