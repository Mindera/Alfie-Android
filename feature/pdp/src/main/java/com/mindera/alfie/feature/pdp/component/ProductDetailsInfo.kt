package com.mindera.alfie.feature.pdp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.mindera.alfie.designsystem.component.price.Price
import com.mindera.alfie.designsystem.component.price.PriceSize
import com.mindera.alfie.designsystem.component.shimmer.shimmer
import com.mindera.alfie.designsystem.component.swatch.SwatchType
import com.mindera.alfie.designsystem.theme.Theme
import com.mindera.alfie.designsystem.tokens.LocalTheme
import com.mindera.alfie.feature.pdp.R
import com.mindera.alfie.feature.pdp.model.ProductDetailsUIState
// Product Colors Selector (screens file): 20dp swatch with a 1px surface/background-terciary
// stroke inside a 24dp tappable box, then a 24dp-wide "+N" counter in body/medium.
private val SUMMARY_SWATCH_SIZE = 20.dp
private val SUMMARY_BOX_SIZE = 24.dp
private val SUMMARY_COUNTER_WIDTH = 24.dp

@Composable
internal fun ProductDetailsInfo(
    state: ProductDetailsUIState.Data,
    onColourSummaryClick: () -> Unit
) {
    val c = LocalTheme.current.primitive.colors
    val details = state.details
    val isLoading = state is ProductDetailsUIState.Data.Loading
    val hasColourChoice = details.colors.size > 1

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // The Figma stacks brand (label/small, content-secondary), name (body/medium) and price
        // (body/medium-bold) with no explicit gaps — the line heights carry the rhythm (16/24/24).
        Column(modifier = Modifier.weight(1f)) {
            if (details.brand.isNotBlank()) {
                // No content/content-secondary alias exists yet (Figma #2B2B2B = neutrals600);
                // using the primitive directly until the token lands — same exception iOS shipped.
                Text(
                    text = details.brand,
                    style = LocalTheme.current.typography.label.small,
                    color = c.neutrals600
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer(
                        isShimmering = isLoading,
                        xScale = Theme.scale.scale80
                    ),
                text = details.name,
                style = LocalTheme.current.typography.body.medium,
                color = c.neutrals800
            )
            Price(
                item = details.price,
                size = PriceSize.Medium,
                modifier = Modifier.shimmer(isShimmering = isLoading)
            )
        }
        if (hasColourChoice && !isLoading) {
            ColourSummary(
                swatch = details.selectedColorUI?.type,
                remainingCount = details.colors.size - 1,
                onClick = onColourSummaryClick
            )
        }
    }
}

@Composable
private fun ColourSummary(
    swatch: SwatchType?,
    remainingCount: Int,
    onClick: () -> Unit
) {
    val c = LocalTheme.current.primitive.colors
    val remainingLabel = stringResource(id = R.string.product_details_colour_summary_more, remainingCount)
    // "+N" is a visual shorthand; screen readers get the action it performs instead.
    val a11yLabel = stringResource(id = R.string.product_details_colour_summary_a11y, remainingCount)

    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .semantics { contentDescription = a11yLabel },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(SUMMARY_BOX_SIZE),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(SUMMARY_SWATCH_SIZE)
                    .border(
                        width = LocalTheme.current.primitive.border.weightDefault,
                        color = c.neutrals300,
                        shape = CircleShape
                    )
                    .background(swatch.swatchColor())
            )
        }
        Text(
            text = remainingLabel,
            style = LocalTheme.current.typography.body.medium,
            color = c.neutrals800,
            modifier = Modifier.width(SUMMARY_COUNTER_WIDTH)
        )
    }
}

private fun SwatchType?.swatchColor(): Color = when (this) {
    is SwatchType.PlainColor -> color
    is SwatchType.Image -> Color.Transparent
    null -> Color.Transparent
}
