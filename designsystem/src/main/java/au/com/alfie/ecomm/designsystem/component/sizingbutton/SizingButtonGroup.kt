package au.com.alfie.ecomm.designsystem.component.sizingbutton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEvent
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.component.button.Button
import au.com.alfie.ecomm.designsystem.component.button.ButtonType
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val MAX_ITEMS_PER_LINE = 3
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
                    Theme.spacing.spacing16.toPx() * 2
                }

                val itemsFullWidth = size.width - sumSpacings
                itemWidth = with(density) {
                    (itemsFullWidth / MAX_ITEMS_PER_LINE).toDp()
                }
            },
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.spacing12),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing16),
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
    val modifier = Modifier.width(width = buttonWidth)
    when (state) {
        SizingButtonState.Selectable -> {
            if (isSelected) {
                Button(
                    modifier = modifier,
                    type = ButtonType.Secondary,
                    text = buttonText,
                    overrideBorderThickness = 2.dp,
                    shape = Theme.shape.small,
                    overrideTextStyle = Theme.typography.paragraph,
                    onClick = { Unit }
                )
            } else {
                Button(
                    modifier = modifier,
                    type = ButtonType.Secondary,
                    text = buttonText,
                    shape = Theme.shape.small,
                    overrideColors = ButtonColors(
                        containerColor = Theme.color.white,
                        contentColor = Theme.color.primary.mono200,
                        disabledContainerColor = Theme.color.white,
                        disabledContentColor = Theme.color.primary.mono200
                    ),
                    overrideTextColor = Theme.color.primary.mono700,
                    overrideTextStyle = Theme.typography.paragraph,
                    onClick = { onClick() }
                )
            }
        }

        SizingButtonState.OutOfStock -> {
            Button(
                type = ButtonType.Secondary,
                text = buttonText,
                isEnabled = false,
                shape = Theme.shape.small,
                modifier = modifier,
                overrideColors = ButtonColors(
                    containerColor = Theme.color.primary.mono050,
                    contentColor = Theme.color.primary.mono100,
                    disabledContainerColor = Theme.color.primary.mono050,
                    disabledContentColor = Theme.color.primary.mono100
                ),
                overrideTextDisabledColor = Theme.color.primary.mono300,
                overrideTextStyle = Theme.typography.paragraph,
                onClick = { Unit }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun SizingButtonsPreview() {
    val options = persistentListOf(
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
    )
    Theme {
        SizingButtonGroup(
            selectedIndex = 1,
            options = options,
            onSelectedOption = { }
        )
    }
}
