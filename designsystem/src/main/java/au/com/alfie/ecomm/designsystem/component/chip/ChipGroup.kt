package au.com.alfie.ecomm.designsystem.component.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import au.com.alfie.ecomm.core.ui.event.ClickEventOneArg
import au.com.alfie.ecomm.designsystem.theme.Theme

@Composable
fun ChipGroup(
    chips: List<ChipProperties>,
    onSelectionChange: ClickEventOneArg<Int>,
    modifier: Modifier = Modifier,
    onDismiss: ClickEventOneArg<Int> = {},
    chipSpacing: Dp = Theme.spacing.spacing8,
    horizontalSpacing: Dp = Theme.spacing.spacing8
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(chipSpacing),
        state = rememberLazyListState()
    ) {
        item { Spacer(modifier = Modifier.width(horizontalSpacing)) }
        itemsIndexed(chips) { index, chip ->
            Chip(
                label = chip.label,
                counter = chip.counter,
                onClickEvent = { onSelectionChange(index) },
                isSelected = chip.isSelected,
                isEnabled = chip.isEnabled,
                isDismissible = chip.isDismissible,
                onDismiss = { onDismiss(index) }
            )
        }
        item { Spacer(modifier = Modifier.width(horizontalSpacing)) }
    }
}
