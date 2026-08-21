package com.mindera.alfie.feature.pdp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mindera.alfie.core.ui.event.ClickEventOneArg
import com.mindera.alfie.designsystem.component.swatch.SwatchType
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.pdp.R
import com.mindera.alfie.feature.pdp.model.ColorUI
import com.mindera.alfie.feature.pdp.model.ProductDetailsEvent
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState
private const val MAX_ITEMS_PER_LINE = 3

// Inline colour cards (PDP colour selection): square cards, three to a row on the 8px screen
// gutter, mirroring the size-selector chip language — 1px border/soft, selection a heavier
// 2px border, sold-out colours dimmed to 50% and un tappable. Swatch is the same 20dp
// surface/background-terciary-stroked circle as the info-block colour summary.
private val CARD_BORDER = 1.dp
private val CARD_BORDER_SELECTED = 2.dp
private val CARD_SWATCH_SIZE = 20.dp
private val CARD_INNER_PADDING = 8.dp
private val CARD_GAP = 8.dp
private const val DISABLED_ALPHA = 0.5F

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ProductDetailsColourCards(
    state: ProductDetailsUIState.Data,
    onColorClick: ClickEventOneArg<ProductDetailsEvent.OnColorClick>
) {
    val c = LocalTheme.current.primitive.colors
    var itemWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.product_details_select_a_colour),
            style = LocalTheme.current.typography.heading.xSmall,
            color = c.neutrals800
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Theme.spacing.spacing8)
                .onSizeChanged { size ->
                    val sumSpacings = with(density) {
                        CARD_GAP.toPx() * (MAX_ITEMS_PER_LINE - 1)
                    }
                    itemWidth = with(density) {
                        ((size.width - sumSpacings) / MAX_ITEMS_PER_LINE).toDp()
                    }
                },
            verticalArrangement = Arrangement.spacedBy(CARD_GAP),
            horizontalArrangement = Arrangement.spacedBy(CARD_GAP),
            maxItemsInEachRow = MAX_ITEMS_PER_LINE
        ) {
            state.details.colors.forEach { color ->
                ColourCard(
                    color = color,
                    isSelected = state.details.selectedColorUI?.index == color.index,
                    cardWidth = itemWidth,
                    onClick = { onColorClick(ProductDetailsEvent.OnColorClick(color.index)) }
                )
            }
        }
    }
}

@Composable
private fun ColourCard(
    color: ColorUI,
    isSelected: Boolean,
    cardWidth: Dp,
    onClick: () -> Unit
) {
    val c = LocalTheme.current.primitive.colors
    val isEnabled = color.type.isEnabled
    val borderWeight = if (isSelected) CARD_BORDER_SELECTED else CARD_BORDER

    Column(
        modifier = Modifier
            .width(cardWidth)
            .alpha(if (isEnabled) 1F else DISABLED_ALPHA)
            .border(
                width = borderWeight,
                color = c.neutrals200,
                shape = Theme.shape.none
            )
            .then(
                if (isEnabled) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(CARD_INNER_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(CARD_SWATCH_SIZE)
                .border(
                    width = LocalTheme.current.primitive.border.weightDefault,
                    color = c.neutrals300,
                    shape = CircleShape
                )
                .background(color.type.swatchColor())
        )
        Text(
            text = color.id,
            style = LocalTheme.current.typography.body.medium,
            color = c.neutrals800,
            modifier = Modifier.padding(top = Theme.spacing.spacing8)
        )
    }
}

private fun SwatchType.swatchColor(): Color = when (this) {
    is SwatchType.PlainColor -> color
    is SwatchType.Image -> Color.Transparent
}
