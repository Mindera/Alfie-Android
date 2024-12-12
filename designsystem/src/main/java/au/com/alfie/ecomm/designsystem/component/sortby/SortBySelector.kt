package au.com.alfie.ecomm.designsystem.component.sortby

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.core.ui.event.ClickEventTwoArg
import au.com.alfie.ecomm.designsystem.R
import au.com.alfie.ecomm.designsystem.theme.Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun SortBySelector(
    selectedIndex: Int,
    items: ImmutableList<SortByItem>,
    onItemSelection: ClickEventOneArg<Int>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = Theme.spacing.spacing16),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.spacing6)
    ) {
        items.forEachIndexed { index, item ->
            SortByButton(
                item = item,
                isSelected = index == selectedIndex,
                onClick = { startPosition, endPosition ->
                    onItemSelection(index)
                    // Scroll to the item if it is out of the screen
                    if (startPosition < scrollState.value || endPosition > scrollState.value + scrollState.viewportSize) {
                        coroutineScope.launch { scrollState.animateScrollTo(startPosition) }
                    }
                }
            )
        }
    }
}

@Composable
private fun SortByButton(
    item: SortByItem,
    isSelected: Boolean,
    onClick: ClickEventTwoArg<Int, Int>
) {
    var startPosition by remember { mutableIntStateOf(0) }
    var endPosition by remember { mutableIntStateOf(0) }
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Theme.color.primary.mono900 else Theme.color.primary.mono200,
        label = "SortByBorderColor"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = Theme.spacing.spacing4)
            .heightIn(36.dp)
            .background(Theme.color.white)
            .clip(Theme.shape.small)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = Theme.shape.small
            )
            .clickable { onClick(startPosition, endPosition) }
            .onGloballyPositioned {
                startPosition = it.boundsInParent().left.toInt()
                endPosition = it.boundsInParent().right.toInt()
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (item.icon != null) {
                Spacer(modifier = Modifier.width(Theme.spacing.spacing12))
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = null,
                    modifier = Modifier.size(Theme.iconSize.small)
                )
                Spacer(modifier = Modifier.width(Theme.spacing.spacing6))
            } else {
                Spacer(modifier = Modifier.width(Theme.spacing.spacing16))
            }
            Text(
                text = item.text,
                style = Theme.typography.smallBold
            )
            Spacer(modifier = Modifier.width(Theme.spacing.spacing16))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SortBySelectorPreview() {
    val items = persistentListOf(
        SortByItem(
            text = "Most Popular",
            icon = R.drawable.ic_action_heart_outline
        ),
        SortByItem(
            text = "Price - High to Low",
            icon = R.drawable.ic_informational_sale
        ),
        SortByItem(
            text = "Price - Low to High",
            icon = R.drawable.ic_informational_sale
        )
    )

    Theme {
        var selectedItem by remember { mutableIntStateOf(0) }

        SortBySelector(
            selectedIndex = selectedItem,
            items = items,
            onItemSelection = { selectedItem = it }
        )
    }
}
